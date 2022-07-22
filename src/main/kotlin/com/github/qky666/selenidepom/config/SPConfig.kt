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
 * Some values (pomVersion) can be set, in precedence order:
 * 1) Programmatically.
 * 2) As System property ("selenide-pom.<propertyName>").
 * 3) In selenide-pom.properties file.
 * Some values (selenideConfig) may only be set programmatically.
 */
object SPConfig {
    private const val fileName = "selenide-pom.properties"
    private const val defaultPomVersion = "default"
    @Suppress("MemberVisibilityCanBePrivate")
    const val defaultDesktopPomVersion = "desktop"
    @Suppress("MemberVisibilityCanBePrivate")
    const val defaultMobilePomVersion = "mobile"
    @Suppress("MemberVisibilityCanBePrivate")
    const val defaultDeviceName = "Nexus 5"

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
     * The default pomVersion (thread local value) used in [com.github.qky666.selenidepom.pom.Page.shouldLoadRequired]
     * and [com.github.qky666.selenidepom.pom.Page.hasLoadedRequired] methods.
     * Default value: "selenide-pom.pomVersion" System property if defined, "selenide-pom.pomVersion" value in selenide-pom.properties if defined,
     * or [defaultPomVersion] in other case.
     */
    var pomVersion: String
        get() = threadLocalPomVersion.get()
        set(value) { threadLocalPomVersion.set(value) }

    private val threadLocalSelenideConfig: ThreadLocal<SelenideConfig> = ThreadLocal.withInitial { SelenideConfig() }

    /**
     * The selenideConfig (thread local value).
     * Default value: The default [com.codeborne.selenide.Configuration] obtained from System properties and Selenide properties file
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var selenideConfig: SelenideConfig
        get() = threadLocalSelenideConfig.get()
        set(value) { threadLocalSelenideConfig.set(value) }

    /**
     * Adds mobile emulation and sets browser to chrome to the thread local selenideConfig instance.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    @JvmOverloads
    fun addMobileEmulation(deviceName: String = defaultDeviceName) {
        val config = threadLocalSelenideConfig.get()
        config.browser("chrome")
        val chromeOptions = ChromeOptions()
        chromeOptions.setExperimentalOption("mobileEmulation", mapOf("deviceName" to deviceName))
        val newCapabilities = chromeOptions.merge(config.browserCapabilities())
        config.browserCapabilities(newCapabilities)
    }

    /**
     * Creates a new WebDriver based on thread local selenideConfig configuration
     * and tells Selenide to use this instance.
     *
     * @param proxy Proxy passed to webDriverFactory.createWebDriver, usually left to null
     * @param browserDownloadsFolder File passed to webDriverFactory.createWebDriver, usually left to null
     */
    @Suppress("MemberVisibilityCanBePrivate")
    @JvmOverloads
    fun setWebDriver(proxy: Proxy? = null, browserDownloadsFolder: File? = null) {
        val driver = webDriverFactory.createWebDriver(threadLocalSelenideConfig.get(), proxy, browserDownloadsFolder)
        WebDriverRunner.setWebDriver(driver)
    }

    /**
     * Resets current thread selenideConfig to the default [com.codeborne.selenide.Configuration] obtained
     * from System properties and Selenide properties file
     */
    fun resetSelenideConfig() {
        selenideConfig = SelenideConfig()
        pomVersion = System.getProperty(
            "selenide-pom.pomVersion",
            fileProperties.getProperty("selenide-pom.pomVersion", defaultPomVersion)
        )
    }

    /**
     * Creates a new basic desktop WebDriver of type 'browser' based on thread local selenideConfig configuration,
     * tells Selenide to use this instance, and sets the default pomVersion to use.
     *
     * @param browser The type of WebDriver to create (chrome, firefox, edge, etc.)
     * @param pomVersion The default pomVersion to use
     */
    @JvmOverloads
    fun setupBasicDesktopBrowser(
        browser: String = selenideConfig.browser(),
        pomVersion: String = defaultDesktopPomVersion
    ) {
        resetSelenideConfig()
        selenideConfig.browser(browser)
        SPConfig.pomVersion = pomVersion
        setWebDriver()
    }

    /**
     * Creates a new basic mobile WebDriver based on thread local selenideConfig configuration
     * and adds mobile emulation using the given deviceName to it, tells Selenide to use this instance,
     * and sets the default pomVersion to use.
     *
     * @param deviceName The type of WebDriver to create (chrome, firefox, edge, etc.)
     * @param pomVersion The default pomVersion to use
     */
    @JvmOverloads
    fun setupBasicMobileBrowser(deviceName: String = defaultDeviceName, pomVersion: String = defaultMobilePomVersion) {
        resetSelenideConfig()
        selenideConfig.browser("chrome")
        addMobileEmulation(deviceName)
        SPConfig.pomVersion = pomVersion
        setWebDriver()
    }
}
