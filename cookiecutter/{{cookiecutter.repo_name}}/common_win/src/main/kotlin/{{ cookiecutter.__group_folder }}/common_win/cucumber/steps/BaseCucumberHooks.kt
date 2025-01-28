package {{ cookiecutter.group }}.common_win.cucumber.steps

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import {{ cookiecutter.group }}.common_win.util.CucumberHelper
import {{ cookiecutter.group }}.common_win.util.WindowsDriverProvider.Companion.createDriverForApp
import io.cucumber.java8.Es
import io.cucumber.java8.Scenario
import org.apache.logging.log4j.kotlin.Logging

open class BaseCucumberHooks : Es, Logging {

    init {
        this.Before { scenario: Scenario ->
            SPConfig.resetConfig()

            // Configure driver
            SPConfig.setWebDriver(createDriverForApp())

            // Set env
            TestData.init("test")

            // Set scenario
            CucumberHelper.scenario = scenario
        }

        this.After { _ ->
            // Quit driver
            SPConfig.quitDriver()
        }
    }
}
