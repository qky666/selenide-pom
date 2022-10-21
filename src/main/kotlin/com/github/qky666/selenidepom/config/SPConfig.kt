package com.github.qky666.selenidepom.config

import com.codeborne.selenide.SelenideConfig
import com.codeborne.selenide.WebDriverRunner
import com.codeborne.selenide.webdriver.WebDriverFactory
import org.openqa.selenium.Proxy
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File
import java.util.*


/**
 * Main configuration values.
 *
 * Some values ([pomVersion], [lang]) can be set, in precedence order:
 * 1) Programmatically.
 * 2) As System property (`selenide-pom.<propertyName>`).
 * 3) In `selenide-pom.properties` file.
 * Some values ([selenideConfig]) may only be set programmatically.
 */
object SPConfig {
    private const val fileName = "selenide-pom.properties"
    private const val defaultPomVersion = "default"
    const val defaultDesktopPomVersion = "desktop"
    const val defaultMobilePomVersion = "mobile"
    const val defaultDeviceName = "Nexus 5"
    private const val defaultLang = "default"

    private val fileProperties = Properties()
    private val webDriverFactory = WebDriverFactory()

    init {
        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
        if (inputStream != null) {
            fileProperties.load(inputStream)
        }
    }

    private val threadLocalPomVersion: ThreadLocal<String> = ThreadLocal.withInitial {
        System.getProperty(
            "selenide-pom.pomVersion", fileProperties.getProperty("selenide-pom.pomVersion", defaultPomVersion)
        )
    }

    /**
     * The default [pomVersion] (thread local value) used in [com.github.qky666.selenidepom.pom.shouldLoadRequired]
     * and [com.github.qky666.selenidepom.pom.hasLoadedRequired] methods.
     * Default value: `selenide-pom.pomVersion` System property if defined,
     * `selenide-pom.pomVersion` value in `selenide-pom.properties` if defined, or [defaultPomVersion] in other case.
     */
    var pomVersion: String
        get() = threadLocalPomVersion.get()
        set(value) {
            threadLocalPomVersion.set(value)
        }

    private val threadLocalLang: ThreadLocal<String> = ThreadLocal.withInitial {
        System.getProperty(
            "selenide-pom.lang", fileProperties.getProperty("selenide-pom.lang", defaultLang)
        )
    }

    /**
     * The default `lang` (thread local value) used in [com.github.qky666.selenidepom.pom.ConditionedElement.shouldMeetCondition] method.
     * Default value: `selenide-pom.lang` System property if defined, `selenide-pom.lang` value in `selenide-pom.properties` if defined,
     * or [defaultLang] in other case.
     */
    var lang: String
        get() = threadLocalLang.get()
        set(value) {
            threadLocalLang.set(value)
        }

    private val threadLocalSelenideConfig: ThreadLocal<SelenideConfig> = ThreadLocal.withInitial { SelenideConfig() }

    /**
     * The [SelenideConfig] (thread local value).
     * Default value: The default [com.codeborne.selenide.Configuration] obtained from System properties and `Selenide` properties file.
     */
    var selenideConfig: SelenideConfig
        get() = threadLocalSelenideConfig.get()
        set(value) {
            threadLocalSelenideConfig.set(value)
        }

    /**
     * Adds mobile emulation and sets browser to Chrome in the thread local [selenideConfig] instance.
     *
     * @param deviceName the device name passed to Chrome to do the mobile emulation. Default value: [defaultDeviceName]
     */
    @JvmOverloads
    fun addMobileEmulation(deviceName: String = defaultDeviceName) {
        selenideConfig.browser("chrome")
        val chromeOptions = ChromeOptions()
        chromeOptions.setExperimentalOption("mobileEmulation", mapOf("deviceName" to deviceName))
        val newCapabilities = chromeOptions.merge(selenideConfig.browserCapabilities())
        selenideConfig.browserCapabilities(newCapabilities)
    }

    /**
     * Creates a new [org.openqa.selenium.WebDriver] based on thread local [selenideConfig] configuration
     * and tells `Selenide` to use this instance.
     *
     * @param proxy [Proxy] passed to `webDriverFactory.createWebDriver`, usually `null`
     * @param browserDownloadsFolder file passed to `webDriverFactory.createWebDriver`, usually `null`
     */
    @JvmOverloads
    fun setWebDriver(proxy: Proxy? = null, browserDownloadsFolder: File? = null) {
        val driver = webDriverFactory.createWebDriver(threadLocalSelenideConfig.get(), proxy, browserDownloadsFolder)
        WebDriverRunner.setWebDriver(driver)
    }

    /**
     * Resets current thread [selenideConfig] to the default [com.codeborne.selenide.Configuration] obtained
     * from System properties and `Selenide` properties file.
     */
    fun resetSelenideConfig() {
        selenideConfig = SelenideConfig()
        pomVersion = System.getProperty(
            "selenide-pom.pomVersion", fileProperties.getProperty("selenide-pom.pomVersion", defaultPomVersion)
        )
    }

    /**
     * Creates a new basic desktop [org.openqa.selenium.WebDriver] of type [browser] based on thread local [selenideConfig] configuration,
     * tells `Selenide` to use this instance, and sets the default [pomVersion] to use.
     *
     * @param browser The type of [org.openqa.selenium.WebDriver] to create (chrome, firefox, edge, etc.)
     * @param pomVersion The default [pomVersion] to use
     */
    @JvmOverloads
    fun setupBasicDesktopBrowser(
        browser: String = selenideConfig.browser(), pomVersion: String = defaultDesktopPomVersion
    ) {
        resetSelenideConfig()
        selenideConfig.browser(browser)
        SPConfig.pomVersion = pomVersion
        setWebDriver()
    }

    /**
     * Creates a new basic mobile [org.openqa.selenium.WebDriver] based on thread local [selenideConfig] configuration
     * and adds mobile emulation using the given deviceName to it, tells `Selenide` to use this instance,
     * and sets the default [pomVersion] to use.
     *
     * @param deviceName The type of WebDriver to create (chrome, firefox, edge, etc.)
     * @param pomVersion The default pomVersion to use
     */
    @JvmOverloads
    fun setupBasicMobileBrowser(deviceName: String = defaultDeviceName, pomVersion: String = defaultMobilePomVersion) {
        resetSelenideConfig()
        addMobileEmulation(deviceName)
        SPConfig.pomVersion = pomVersion
        setWebDriver()
    }
}
