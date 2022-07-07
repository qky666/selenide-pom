package com.github.qky666.selenidepom

import com.codeborne.selenide.SelenideConfig
import com.codeborne.selenide.WebDriverRunner
import com.codeborne.selenide.webdriver.WebDriverFactory
import org.openqa.selenium.Proxy
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.Properties


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
    const val defaultDesktopPomVersion = "desktop"
    const val defaultMobilePomVersion = "mobile"
    const val defaultDeviceName = "Nexus 5"
    private val properties = Properties()
    private val webDriverFactory = WebDriverFactory()

    init {
        try {
            properties.load(FileReader(ClassLoader.getSystemResource(fileName).file))
        } catch (ignored: IOException) {
        }
    }

    private val pomVersion: ThreadLocal<String> = ThreadLocal.withInitial {
        System.getProperty(
            "selenide-pom.pomVersion", properties.getProperty("selenide-pom.pomVersion", defaultPomVersion)
        )
    }

    /**
     * Returns the default pomVersion (thread local value) used in [Page.shouldLoadRequired] and [Page.hasLoadedRequired] methods.
     * Default value: "selenide-pom.pomVersion" System property if defined, "selenide-pom.pomVersion" value in selenide-pom.properties if defined,
     * or [defaultPomVersion] in other case.
     *
     * @return the pomVersion.
     */
    fun getPomVersion(): String {
        return pomVersion.get()
    }

    /**
     * Sets the default pomVersion (thread local value) used in [Page.shouldLoadRequired] and [Page.hasLoadedRequired] methods.
     *
     * @param value The new value.
     */
    @Suppress("unused")
    fun setPomVersion(value: String) {
        pomVersion.set(value)
    }

    private val selenideConfig: ThreadLocal<SelenideConfig> = ThreadLocal.withInitial { SelenideConfig() }

    /**
     * Returns the selenideConfig (thread local value).
     * Default value: The default [com.codeborne.selenide.Configuration] obtained from System properties and Selenide properties file
     *
     * @return thread local selenideConfig.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun getSelenideConfig(): SelenideConfig {
        return selenideConfig.get()
    }

    /**
     * Sets the selenideConfig for current Thread.
     *
     * @param value The new value.
     */
    @Suppress("unused")
    fun setSelenideConfig(value: SelenideConfig) {
        selenideConfig.set(value)
    }

    /**
     * Adds mobile emulation and sets browser to chrome to the thread local selenideConfig instance.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun addMobileEmulation(deviceName: String = defaultDeviceName) {
        val config = selenideConfig.get()
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
    fun setWebDriver(proxy: Proxy? = null, browserDownloadsFolder: File? = null) {
        val driver = webDriverFactory.createWebDriver(selenideConfig.get(), proxy, browserDownloadsFolder)
        WebDriverRunner.setWebDriver(driver)
    }

    /**
     * Resets current thread selenideConfig to the default [com.codeborne.selenide.Configuration] obtained
     * from System properties and Selenide properties file
     */
    fun resetSelenideConfig() {
        selenideConfig.set(SelenideConfig())
        setPomVersion(
            System.getProperty(
                "selenide-pom.pomVersion", properties.getProperty("selenide-pom.pomVersion", defaultPomVersion)
            )
        )
    }

    /**
     * Creates a new basic desktop WebDriver of type 'browser' based on thread local selenideConfig configuration,
     * tells Selenide to use this instance, and sets the default pomVersion to use.
     *
     * @param browser The type of WebDriver to create (chrome, firefox, edge, etc.)
     * @param pomVersion The default pomVersion to use
     */
    fun setupBasicDesktopBrowser(
        browser: String = getSelenideConfig().browser(),
        pomVersion: String = defaultDesktopPomVersion
    ) {
        resetSelenideConfig()
        getSelenideConfig().browser(browser)
        setPomVersion(pomVersion)
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
    fun setupBasicMobileBrowser(deviceName: String = defaultDeviceName, pomVersion: String = defaultMobilePomVersion) {
        resetSelenideConfig()
        getSelenideConfig().browser("chrome")
        addMobileEmulation(deviceName)
        setPomVersion(pomVersion)
        setWebDriver()
    }
}
