package {{ cookiecutter.group }}.common_web.cucumber.steps

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import {{ cookiecutter.group }}.common_web.util.CucumberHelper
import io.cucumber.java8.Es
import io.cucumber.java8.Scenario
import org.apache.logging.log4j.kotlin.Logging

open class BaseCucumberHooks : Es, Logging {

//    private val platform = Platform.WINDOWS.toString()
//    private val home = System.getProperty("user.home")
//    private val chromedriverPath = "$home/scoop/apps/chromedriver/current/chromedriver.exe"

    init {
        this.Before { scenario: Scenario ->
            SPConfig.resetConfig()

            // Set env
            TestData.init("test")

            // Configure webdriver
            val browser = getBrowserFromTestName(scenario.name)
            val model = getModelFromTestName(scenario.name)
            val lang = getLanguageFromTestName(scenario.name)
            when (model) {
                "mobile" -> SPConfig.setupBasicMobileBrowser()
                "desktop" -> SPConfig.setupBasicDesktopBrowser(browser)
                else -> throw RuntimeException("Model ${SPConfig.model} not found")
            }
//            val config = SPConfig.selenideConfig
//            config.remote()?.let {
//                val options = if (browser.equals("firefox", ignoreCase = true)) {
//                    // Static (default) port sometimes throws errors with multiple threads
//                    val port = (2828..3828).random()
//                    GeckoOptions().setMarionettePort(port)
//                } else if (browser.equals("chrome", ignoreCase = true)) {
//                    // Static (default) port sometimes throws errors with multiple threads
//                    val port = (9515..10515).random()
//                    val base = BaseOptions().setAutomationName("Chromium").withBrowserName(browser)
//                    base.setCapability("appium:executable", chromedriverPath)
//                    base.setCapability("appium:chromedriverPort", port)
//                    base
//                } else {
//                    throw RuntimeException("Invalid browser: $browser")
//                }
//                options.setPlatformName(platform)
//                val merge = config.browserCapabilities().merge(options)
//                config.browserCapabilities(merge)
//            }
            SPConfig.setDriver()

            // Language
            SPConfig.lang = lang

            // Set scenario
            CucumberHelper.scenario = scenario
        }

        this.After { scenario ->
            // Page source
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
