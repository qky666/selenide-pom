package {{ cookiecutter.group }}.gradle

import com.codeborne.selenide.Condition.be
import com.codeborne.selenide.Condition.have
import com.codeborne.selenide.Condition.not
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import io.qameta.allure.gradle.base.AllureExtension
import io.qameta.allure.gradle.report.AllureAggregateReportPlugin
import io.qameta.allure.gradle.report.AllureReportPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.task
import report
import ru.vyarus.gradle.plugin.python.task.PythonTask
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
import kotlin.io.path.Path


interface AllureExtrasPluginExtension {
    val allureReportDir: DirectoryProperty
    val allureResultsDir: DirectoryProperty
    val allureCombineDir: DirectoryProperty
    val archivedReportsDir: DirectoryProperty
    val summaryLang: Property<String>
}

class AllureExtrasPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<AllureExtrasPluginExtension>("allureExtras")

        val baseAllureReportDir = project.extensions.getByType(AllureExtension::class.java).report.reportDir
        if (project.plugins.hasPlugin(AllureAggregateReportPlugin::class.java)) {
            extension.allureReportDir.convention(baseAllureReportDir.dir(AllureAggregateReportPlugin.AGGREGATE_CONFIGURATION))
        } else {
            extension.allureReportDir.convention(baseAllureReportDir.dir(AllureReportPlugin.AGGREGATE_CONFIGURATION))
        }
        extension.allureCombineDir.convention(baseAllureReportDir.dir("allureCombine"))
        extension.archivedReportsDir.convention(project.layout.projectDirectory.dir("reports"))
        extension.allureResultsDir.convention(project.layout.buildDirectory.dir("allure-results"))
        extension.summaryLang.convention("es")

        createImportAllureHistoryTask(project, extension)
        val allureCombineTask = createAllureCombineTask(project, extension)
        createExportAllureHistoryTask(project, extension, allureCombineTask)
    }

    private fun createImportAllureHistoryTask(project: Project, extension: AllureExtrasPluginExtension): Copy {
        val task = project.task<Copy>("importAllureHistory") {
            group = "verification"
            description = "Copy previous Allure history files from archived reports"
            from(extension.archivedReportsDir.dir("last/history").get().asFile)
            into(extension.allureResultsDir.dir("history").get().asFile)
            mustRunAfter(project.tasks.named("test"))
        }

        project.tasks.findByName(AllureAggregateReportPlugin.REPORT_TASK_NAME)?.mustRunAfter(task)
        project.tasks.findByName(AllureAggregateReportPlugin.SERVE_TASK_NAME)?.mustRunAfter(task)
        project.tasks.findByName(AllureReportPlugin.REPORT_TASK_NAME)?.mustRunAfter(task)
        project.tasks.findByName(AllureReportPlugin.SERVE_TASK_NAME)?.mustRunAfter(task)

        return task
    }

    private fun createAllureCombineTask(project: Project, extension: AllureExtrasPluginExtension): PythonTask {
        return project.task<PythonTask>("allureCombine") {
            group = "verification"
            description = "Combine Allure report in a single HTML file"
            val reportDir = extension.allureReportDir.asFile.get().canonicalPath
            val combineDir = extension.allureCombineDir.asFile.get().canonicalPath
            command =
                "-m allure_combine.combine $reportDir --dest $combineDir --auto-create-folders --remove-temp-files"

            project.tasks.findByName(AllureAggregateReportPlugin.REPORT_TASK_NAME)?.let { mustRunAfter(it) }
            project.tasks.findByName(AllureAggregateReportPlugin.SERVE_TASK_NAME)?.let { mustRunAfter(it) }
            project.tasks.findByName(AllureReportPlugin.REPORT_TASK_NAME)?.let { mustRunAfter(it) }
            project.tasks.findByName(AllureReportPlugin.SERVE_TASK_NAME)?.let { mustRunAfter(it) }
        }
    }

    private fun createExportAllureHistoryTask(
        project: Project,
        extension: AllureExtrasPluginExtension,
        allureCombineTask: PythonTask
    ): Copy {
        return project.task<Copy>("exportAllureHistory") {
            group = "verification"
            description = "Copy generated Allure report and history files to archived reports"
            val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
            into(extension.archivedReportsDir.asFile)
            // history
            into(Path("last", "history").toFile()) {
                from(extension.allureReportDir.dir("history").get().asFile)
            }
            into(Path(now, "history").toFile()) {
                from(extension.allureReportDir.dir("history").get().asFile)
            }
            // report
            into("last") {
                from(extension.allureCombineDir.asFile)
                rename { it.replaceFirst("complete", "complete_$now") }
            }
            into(now) {
                from(extension.allureCombineDir.asFile)
                rename { it.replaceFirst("complete", "complete_$now") }
            }
            project.tasks.findByName(AllureAggregateReportPlugin.REPORT_TASK_NAME)?.let { mustRunAfter(it) }
            project.tasks.findByName(AllureAggregateReportPlugin.SERVE_TASK_NAME)?.let { mustRunAfter(it) }
            project.tasks.findByName(AllureReportPlugin.REPORT_TASK_NAME)?.let { mustRunAfter(it) }
            project.tasks.findByName(AllureReportPlugin.SERVE_TASK_NAME)?.let { mustRunAfter(it) }
            mustRunAfter(allureCombineTask)

            doFirst {
                extension.archivedReportsDir.dir("last").get().asFile.deleteRecursively()
            }

            doLast {
                // Save summary image
                val reportCombinePath = extension.allureCombineDir.file("complete.html").get().asFile.canonicalPath
                Configuration.baseUrl = "file:///$reportCombinePath"
                Configuration.browser = "chrome"
                Configuration.headless = true
                Configuration.timeout = 10000
                Configuration.screenshots = false
                Selenide.open("")
                // Switch to desired language
                val lang = extension.summaryLang.get()
                Selenide.element("button[data-ga4-event=language_menu_click]").click()
                Selenide.element("div.language-select li[data-id=$lang]").scrollIntoView(true).click()
                val overview = Selenide.element("div.app__content").should(be(visible), not(have(text("Loading"))))
                    .scrollIntoView(true)
                TimeUnit.SECONDS.sleep(2)
                overview.screenshotAsImage()?.let {
                    val summaryFileName = "summary_$now.png"
                    val summaryLastFile = extension.archivedReportsDir.dir("last").get().file(summaryFileName).asFile
                    val summaryNowFile = extension.archivedReportsDir.dir(now).get().file(summaryFileName).asFile
                    ImageIO.write(it, "png", summaryLastFile)
                    ImageIO.write(it, "png", summaryNowFile)
                }
                Selenide.closeWindow()
                Selenide.closeWebDriver()
            }
        }
    }
}
