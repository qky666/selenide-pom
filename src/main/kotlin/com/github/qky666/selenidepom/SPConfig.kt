package com.github.qky666.selenidepom

import com.codeborne.selenide.SelenideConfig
import com.codeborne.selenide.webdriver.WebDriverFactory
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
    private val properties = Properties()

    /**
     * A Selenide [com.codeborne.selenide.webdriver.WebDriverFactory] instance. May be util in multithreading environments
     */
    val webDriverFactory = WebDriverFactory()

    init {
        try {
            properties.load(FileReader(fileName))
        } catch (ignored: IOException) {
        }
    }

    private val pomVersion: ThreadLocal<String> = ThreadLocal.withInitial {
        System.getProperty(
            "selenide-pom.pomVersion",
            properties.getProperty("selenide-pom.pomVersion", defaultPomVersion)
        )
    }

    /**
     * Returns the default pomVersion (thread local value) used in [Page.shouldLoadRequired] and [Page.hasLoadedRequired] methods.
     * Default value: "selenide-pom.pomVersion" System property if defined, "selenide-pom.pomVersion" value in selenide-pom.properties if defined,
     * or "default" in other case.
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
    fun setPomVersion(value: String) {
        pomVersion.set(value)
    }

    private val selenideConfig: ThreadLocal<SelenideConfig> = ThreadLocal.withInitial { SelenideConfig() }

    /**
     * Returns the selenideConfig (thread local value).
     * Default value: The default [com.codeborne.selenide.Configuration] obtained from System properties and Selenide properties file
     *
     * @return default pomVersion.
     */
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
}
