package {{ cookiecutter.group }}.common.util

import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.SPConfig
import io.cucumber.java8.Scenario
import io.cucumber.plugin.event.PickleStepTestStep
import org.apache.logging.log4j.kotlin.Logging
import org.openqa.selenium.OutputType

object CucumberHelper : Logging {
    private val scenarioThreadLocal: ThreadLocal<Scenario?> = ThreadLocal.withInitial { null }
    var scenario: Scenario?
        get() = scenarioThreadLocal.get()
        set(value) = scenarioThreadLocal.set(value)

    private val stepThreadLocal: ThreadLocal<PickleStepTestStep?> = ThreadLocal.withInitial { null }
    var step: PickleStepTestStep?
        get() = stepThreadLocal.get()
        set(value) = stepThreadLocal.set(value)

    @Suppress("unused")
    fun attachScreenshot(name: String = "Page screenshot", currentScenario: Scenario? = scenario) {
        if (currentScenario == null) {
            logger.warn { "No scenario received" }
        } else {
            val screenshot = Selenide.screenshot(OutputType.BYTES)
            if (screenshot != null) {
                currentScenario.attach(screenshot, "image/png", name)
            } else {
                val message = "No screenshot available"
                currentScenario.log(message)
            }
        }
    }

    fun attachPageSource(currentScenario: Scenario? = scenario) {
        SPConfig.getWebDriver()?.let {
            currentScenario?.attach(it.pageSource, "text/html;charset=utf-8", "source")
        }
    }
}
