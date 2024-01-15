package {{ cookiecutter.group }}.cucumber.runner

import io.cucumber.testng.AbstractTestNGCucumberTests
import io.cucumber.testng.CucumberOptions
import org.apache.logging.log4j.kotlin.Logging
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
@CucumberOptions(
    features = ["@build/test-results/cucumber/failed_scenarios.txt"],
    glue = ["{{ cookiecutter.group }}.cucumber.steps"],
    plugin = ["{{ cookiecutter.group }}.cucumber.listener.CucumberListener", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", "pretty", "json:build/test-results/cucumber/rerun.json", "rerun:build/test-results/cucumber/failed_again_scenarios.txt"],
    tags = "(not @skip or @force) and @retyr",
)
class CucumberTestNgFailedTestRunner : AbstractTestNGCucumberTests(), Logging {
    @DataProvider(parallel = true)
    override fun scenarios(): Array<out Array<Any>>? {
        logger.debug { "Starting scenarios" }
        return super.scenarios()
    }
}
