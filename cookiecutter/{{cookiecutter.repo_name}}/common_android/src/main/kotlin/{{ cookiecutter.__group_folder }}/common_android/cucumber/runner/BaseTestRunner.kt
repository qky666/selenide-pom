package {{ cookiecutter.group }}.common_android.cucumber.runner

import io.cucumber.testng.AbstractTestNGCucumberTests
import org.apache.logging.log4j.kotlin.Logging
import org.testng.annotations.DataProvider

abstract class BaseTestRunner : AbstractTestNGCucumberTests(), Logging {
    @DataProvider(parallel = true)
    override fun scenarios(): Array<out Array<Any>>? {
        logger.debug { "Starting scenarios" }
        return super.scenarios()
    }
}