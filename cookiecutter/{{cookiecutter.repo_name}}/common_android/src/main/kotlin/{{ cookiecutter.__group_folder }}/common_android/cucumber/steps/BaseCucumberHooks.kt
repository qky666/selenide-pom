package {{ cookiecutter.group }}.common_android.cucumber.steps

import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import {{ cookiecutter.group }}.common_android.util.AndroidDriverProvider.Companion.createDriver
import {{ cookiecutter.group }}.common_android.util.CucumberHelper
import io.cucumber.java8.Es
import io.cucumber.java8.Scenario
import org.apache.logging.log4j.kotlin.Logging

open class BaseCucumberHooks : Es, Logging {

    init {
        this.Before { scenario: Scenario ->
            SPConfig.resetConfig()

            // Set env
            TestData.init("test")

            // Set scenario
            CucumberHelper.scenario = scenario

            // Configure driver
            SPConfig.quitDriver()
            SPConfig.selenideConfig.browserSize(null)
            SPConfig.setWebDriver(createDriver())
        }

        this.After { _ ->
            // Quit driver
            Selenide.closeWebDriver()
        }
    }
}
