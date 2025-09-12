package buildsrc.convention

import com.codeborne.selenide.Condition
import com.codeborne.selenide.ScrollIntoViewOptions
import com.codeborne.selenide.ScrollIntoViewOptions.Block
import com.codeborne.selenide.ScrollIntoViewOptions.Inline
import com.codeborne.selenide.Selenide
import io.qameta.allure.gradle.base.AllureExtension
import io.qameta.allure.gradle.report.AllureAggregateReportPlugin
import io.qameta.allure.gradle.report.AllureReportPlugin
import report
import ru.vyarus.gradle.plugin.python.task.PythonTask
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import kotlin.io.path.Path

plugins {
    java
    kotlin("jvm")
    id("io.qameta.allure-adapter")
    id("ru.vyarus.use-python")
}

// Access the version catalog
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    implementation(libs.findBundle("allureExtrasEcosystem").get())
}


interface AllureExtrasPluginExtension {
    val allureReportDir: DirectoryProperty
    val allureResultsDir: DirectoryProperty
    val allureCombineDir: DirectoryProperty
    val archivedReportsDir: DirectoryProperty
    val summaryLang: Property<String>
}


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

fun createImportAllureHistoryTask(project: Project, extension: AllureExtrasPluginExtension): Copy {
    val provider = project.tasks.register<Copy>("importAllureHistory") {
        group = "verification"
        description = "Copy previous Allure history files from archived reports"
        from(extension.archivedReportsDir.dir("last/history").get().asFile)
        into(extension.allureResultsDir.dir("history").get().asFile)
        mustRunAfter(project.tasks.named("test"))
    }
    val task = provider.get()

    project.tasks.findByName(AllureAggregateReportPlugin.REPORT_TASK_NAME)?.mustRunAfter(task)
    project.tasks.findByName(AllureAggregateReportPlugin.SERVE_TASK_NAME)?.mustRunAfter(task)
    project.tasks.findByName(AllureReportPlugin.REPORT_TASK_NAME)?.mustRunAfter(task)
    project.tasks.findByName(AllureReportPlugin.SERVE_TASK_NAME)?.mustRunAfter(task)

    return task
}

fun createAllureCombineTask(project: Project, extension: AllureExtrasPluginExtension): PythonTask {
    val provider = project.tasks.register<AllureCombineTask>("allureCombine") {
        group = "verification"
        description = "Combine Allure report in a single HTML file"
        reportDir = extension.allureReportDir.asFile.get().canonicalPath
        combineDir = extension.allureCombineDir.asFile.get().canonicalPath

        project.tasks.findByName(AllureAggregateReportPlugin.REPORT_TASK_NAME)?.let { mustRunAfter(it) }
        project.tasks.findByName(AllureAggregateReportPlugin.SERVE_TASK_NAME)?.let { mustRunAfter(it) }
        project.tasks.findByName(AllureReportPlugin.REPORT_TASK_NAME)?.let { mustRunAfter(it) }
        project.tasks.findByName(AllureReportPlugin.SERVE_TASK_NAME)?.let { mustRunAfter(it) }
    }
    return provider.get()
}

fun createExportAllureHistoryTask(
    project: Project, extension: AllureExtrasPluginExtension, allureCombineTask: PythonTask
): Copy {
    val provider = project.tasks.register<Copy>("exportAllureHistory") {
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
            com.codeborne.selenide.Configuration.baseUrl = "file:///$reportCombinePath"
            com.codeborne.selenide.Configuration.browser = "chrome"
            com.codeborne.selenide.Configuration.headless = true
            com.codeborne.selenide.Configuration.timeout = 10000
            com.codeborne.selenide.Configuration.screenshots = false
            Selenide.open("")
            // Switch to desired language
            val lang = extension.summaryLang.get()
            Selenide.element("div.side-nav__footer div.side-nav__item:first-child button").click()
            Selenide.element("div.language-select li[data-id=$lang]")
                .scrollIntoView(instantCenter).click()
            val overview = Selenide.element("div.app__content")
                .should(Condition.be(Condition.visible), Condition.not(Condition.have(Condition.text("Loading"))))
                .scrollIntoView(instantCenter)
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
    return provider.get()
}

abstract class AllureCombineTask : PythonTask() {
    @Input
    var reportDir = "reportDir"

    @Input
    var combineDir = "combineDir"

    override fun getCommand(): Property<Any> {
        val combineCommand = project.objects.property(String::class.java)
        combineCommand.set("-m allure_combine.combine $reportDir --dest $combineDir --auto-create-folders --remove-temp-files")
        @Suppress("UNCHECKED_CAST") return combineCommand as Property<Any>
    }
}