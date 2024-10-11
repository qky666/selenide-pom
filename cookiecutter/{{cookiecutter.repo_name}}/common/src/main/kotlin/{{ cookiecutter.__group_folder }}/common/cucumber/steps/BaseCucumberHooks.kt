package {{ cookiecutter.group }}.common.cucumber.steps

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import {{ cookiecutter.group }}.common.util.CucumberHelper
import io.cucumber.java8.Es
import io.cucumber.java8.Scenario
import org.apache.logging.log4j.kotlin.Logging

open class BaseCucumberHooks : Es, Logging {

    init {
        this.Before { scenario: Scenario ->
            SPConfig.resetConfig()

            // Configure webdriver
            val browser = getBrowserFromTestName(scenario.name)
            val model = getModelFromTestName(scenario.name)
            val lang = getLanguageFromTestName(scenario.name)
            when (model) {
                "mobile" -> SPConfig.setupBasicMobileBrowser()
                "desktop" -> SPConfig.setupBasicDesktopBrowser(browser)
                else -> throw RuntimeException("Model ${SPConfig.model} not found")
            }
            SPConfig.lang = lang
            SPConfig.setDriver()

            // Set env
            TestData.init("prod")

            // Set scenario
            CucumberHelper.scenario = scenario
        }

        this.After { scenario ->
            // Screenshot
            if (scenario.isFailed) {
                CucumberHelper.attachPageSource(scenario)
            }

            // Quit webdriver
            SPConfig.quitDriver()
        }
    }

    private fun getBrowserFromTestName(name: String): String {
        return name.substringAfter("Navegador: ").substringBefore(";")
    }

    private fun getModelFromTestName(name: String): String {
        return name.substringAfter("Modelo: ").substringBefore(";")
    }

    private fun getLanguageFromTestName(name: String): String {
        return name.substringAfter("Idioma: ").substringBefore(";")
    }
}
