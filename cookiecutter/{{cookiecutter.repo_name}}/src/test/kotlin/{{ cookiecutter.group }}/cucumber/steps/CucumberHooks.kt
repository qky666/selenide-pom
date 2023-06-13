@file:Suppress("unused")

package {{ cookiecutter.group }}.cucumber.steps

import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import io.cucumber.java8.Es
import io.cucumber.java8.Scenario
import org.openqa.selenium.OutputType

class CucumberHooks : Es {

    init {
        Before { scenario: Scenario ->
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
            SPConfig.setCurrentThreadDriver()

            // Set env
            TestData.init("prod")
        }

        After { scenario: Scenario ->
            // Attach screenshot
            if (scenario.isFailed) {
                val screenshot = Selenide.screenshot(OutputType.BYTES)
                if (screenshot != null) {
                    scenario.attach(screenshot, "image/png", "Test failed screenshot")
                } else {
                    scenario.log("Scenario is failed but there is no screenshot available in ${scenario.name}")
                }
            }

            // Quit webdriver
            SPConfig.quitCurrentThreadDriver()
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
