package {{ cookiecutter.group }}.common_win.cucumber.listener

import {{ cookiecutter.group }}.common_win.util.AllureReportHelper
import {{ cookiecutter.group }}.common_win.util.CucumberHelper
import io.cucumber.plugin.ConcurrentEventListener
import io.cucumber.plugin.event.EventHandler
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.PickleStepTestStep
import io.cucumber.plugin.event.TestStepFinished
import io.cucumber.plugin.event.TestStepStarted
import io.qameta.allure.Allure
import io.qameta.allure.AllureLifecycle
import org.apache.logging.log4j.kotlin.Logging

@Suppress("unused")
class CucumberListener @JvmOverloads constructor(private val lifecycle: AllureLifecycle = Allure.getLifecycle()) :
    ConcurrentEventListener, Logging {

    private val stepFinishedHandler = EventHandler { event: TestStepFinished -> handleTestStepFinished(event) }
    private val stepStartedHandler = EventHandler { event: TestStepStarted -> handleTestStepStarted(event) }

    // Event Handlers
    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(TestStepFinished::class.java, stepFinishedHandler)
    }

    private fun handleTestStepFinished(event: TestStepFinished) {
        val step = event.testStep
        if (step is PickleStepTestStep) {
            logger.info("Step '$step' finished")
            if (!event.result.status.isOk) {
                AllureReportHelper.attachScreenshot("Test step failed screenshot")
            } else if (event.testCase.tags.contains("@screenshot")) {
                AllureReportHelper.attachScreenshot()
            }
        }
    }

    private fun handleTestStepStarted(event: TestStepStarted) {
        val step = event.testStep
        if (step is PickleStepTestStep) {
            logger.info("Step '$step' started")
            CucumberHelper.step = step
        }
    }
}
