package com.github.qky666.selenidepom.config

import com.codeborne.selenide.Driver
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideConfig
import com.codeborne.selenide.SelenideDriver
import com.codeborne.selenide.WebDriverRunner
import mu.KotlinLogging
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.util.Properties

private const val SELENIDE_POM_PROPERTIES_FILENAME = "selenide-pom.properties"
private const val DEFAULT_MODEL = "default"
const val DEFAULT_DESKTOP_MODEL = "desktop"
const val DEFAULT_MOBILE_MODEL = "mobile"
const val DEFAULT_DEVICE_NAME = "Nexus 5"
private const val DEFAULT_LANG = "default"
private val logger = KotlinLogging.logger {}

/**
 * Main configuration values.
 *
 * Some values ([model], [lang]) can be set, in precedence order:
 * 1) Programmatically.
 * 2) As System property (`selenide-pom.<propertyName>`).
 * 3) In `selenide-pom.properties` file.
 * Some values ([selenideConfig]) may only be set programmatically.
 */
object SPConfig {

    private val fileProperties = Properties()

    init {
        val inputStream =
            Thread.currentThread().contextClassLoader.getResourceAsStream(SELENIDE_POM_PROPERTIES_FILENAME)
        if (inputStream != null) {
            fileProperties.load(inputStream)
        }
    }

    private val threadLocalModel: ThreadLocal<String> = ThreadLocal.withInitial {
        val initial = System.getProperty(
            "selenide-pom.model",
            fileProperties.getProperty("selenide-pom.model", DEFAULT_MODEL)
        )
        logger.debug { "Initial value for SPConfig.model: $initial" }
        initial
    }

    /**
     * The default [model] (thread local value) used in [com.github.qky666.selenidepom.pom.common.shouldLoadRequired]
     * and [com.github.qky666.selenidepom.pom.common.hasLoadedRequired] methods.
     * Default value: `selenide-pom.model` System property if defined,
     * `selenide-pom.model` value in `selenide-pom.properties` if defined, or [DEFAULT_MODEL] in other case.
     */
    var model: String
        get() = threadLocalModel.get()
        set(value) {
            logger.debug { "New value for SPConfig.model: $value" }
            threadLocalModel.set(value)
        }

    private val threadLocalLang: ThreadLocal<String> = ThreadLocal.withInitial {
        val initial = System.getProperty(
            "selenide-pom.lang",
            fileProperties.getProperty("selenide-pom.lang", DEFAULT_LANG)
        )
        logger.debug { "Initial value for SPConfig.lang: $initial" }
        initial
    }

    /**
     * The default `lang` (thread local value) used in some methods in
     * [com.github.qky666.selenidepom.pom.common.LangConditioned] and [com.github.qky666.selenidepom.pom.common.Loadable].
     * Default value: `selenide-pom.lang` System property if defined, `selenide-pom.lang` value
     * in `selenide-pom.properties` if defined, or [DEFAULT_LANG] in other case.
     */
    var lang: String
        get() = threadLocalLang.get()
        set(value) {
            logger.debug { "New value for SPConfig.lang: $value" }
            threadLocalLang.set(value)
        }

    private val threadLocalSelenideConfig: ThreadLocal<SelenideConfig> = ThreadLocal.withInitial { SelenideConfig() }

    /**
     * The [SelenideConfig] (thread local value).
     * Default value: The default [com.codeborne.selenide.Configuration] obtained from System properties
     * and `Selenide` properties file.
     */
    val selenideConfig: SelenideConfig
        get() = threadLocalSelenideConfig.get()

    /**
     * Resets current thread [selenideConfig] to the default [com.codeborne.selenide.Configuration] obtained
     * from System properties and `Selenide` properties file.
     * Also resets [model] to default (read from System property or `Selenide` properties file)
     * and [lang] to default (read from System property or `Selenide` properties file)
     */
    fun resetConfig() {
        threadLocalLang.remove()
        threadLocalModel.remove()
        threadLocalSelenideConfig.remove()
        logger.debug { "SPConfig reset" }
    }

    /**
     * Sets up a basic desktop browser on thread local [selenideConfig] configuration using the provided [browser]
     * (current [selenideConfig] browser by default) and sets the default [model] to use.
     *
     * @param browser The type of [org.openqa.selenium.WebDriver] to create (chrome, firefox, edge, etc.)
     * @param model The default [model] to use
     */
    @JvmOverloads
    fun setupBasicDesktopBrowser(
        browser: String = selenideConfig.browser(),
        model: String = DEFAULT_DESKTOP_MODEL
    ) {
        selenideConfig.browser(browser)
        SPConfig.model = model
    }

    /**
     * Sets up a basic mobile browser on thread local [selenideConfig] configuration using the provided [deviceName]
     * to add mobile emulation and sets the default [model] to use.
     *
     * @param deviceName The type of `WebDriver` to create (chrome, firefox, edge, etc.)
     * @param model The default `model` to use
     */
    @JvmOverloads
    fun setupBasicMobileBrowser(deviceName: String = DEFAULT_DEVICE_NAME, model: String = DEFAULT_MOBILE_MODEL) {
        selenideConfig.browser("chrome")
        val chromeOptions = ChromeOptions()
        chromeOptions.setExperimentalOption("mobileEmulation", mapOf("deviceName" to deviceName))
        val newCapabilities = chromeOptions.merge(selenideConfig.browserCapabilities())
        selenideConfig.browserCapabilities(newCapabilities)
        SPConfig.model = model
    }

    /**
     * Creates a new [Driver] instance based on thread local [selenideConfig] configuration and returns it.
     *
     * @return created [Driver] instance
     */
    fun createDriver(): Driver {
        return SelenideDriver(selenideConfig).driver()
    }

    /**
     * Sets [newDriver] as the current thread [Driver] and returns it.
     * If [newDriver] is `null`, a new [Driver] instance is created using current thread local
     * [selenideConfig] configuration.
     *
     * @return [Driver] instance
     */
    @JvmOverloads
    fun setCurrentThreadDriver(newDriver: Driver? = null): Driver {
        val driver = (newDriver ?: createDriver())

        val webDriver = driver.getAndCheckWebDriver()
        WebDriverRunner.setWebDriver(webDriver)
        return driver
    }

    fun getCurrentWebDriver(): WebDriver? {
        return try {
            Selenide.webdriver().driver().webDriver
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun quitCurrentThreadDriver() {
        // Sometimes Selenide.closeWebDriver() does not close the WebDriver correctly (possible WebDriver bug).
        // Closing every window first is safer.
        val driver = getCurrentWebDriver()
        if (driver != null) {
            for (ignored in driver.windowHandles) {
                driver.close()
            }
        }
        Selenide.closeWebDriver()
    }
}
