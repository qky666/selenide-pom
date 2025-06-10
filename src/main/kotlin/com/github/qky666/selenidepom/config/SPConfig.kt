package com.github.qky666.selenidepom.config

import com.codeborne.selenide.Driver
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideConfig
import com.codeborne.selenide.SelenideDriver
import com.codeborne.selenide.WebDriverRunner
import com.github.qky666.selenidepom.config.SPConfig.selenideConfig
import com.github.qky666.selenidepom.data.ResourceHelper.Companion.getResourceFile
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.WebDriver
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

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

    private val fileProperties = Properties().let {
        getResourceFile(SELENIDE_POM_PROPERTIES_FILENAME)?.let { res ->
            InputStreamReader(FileInputStream(res), StandardCharsets.UTF_8).use { input -> it.load(input) }
        }
        it
    }

    private val threadLocalModel = ThreadLocal.withInitial {
        val default = fileProperties.getProperty("selenide-pom.model", DEFAULT_MODEL)
        val initial = System.getProperty("selenide-pom.model", default)
        logger.debug { "Initial value for SPConfig.model: $initial" }
        initial
    }

    /**
     * The default [model] (thread local value) used in [com.github.qky666.selenidepom.pom.shouldLoadRequired]
     * and [com.github.qky666.selenidepom.pom.hasLoadedRequired] methods.
     * Default value: `selenide-pom.model` System property if defined,
     * `selenide-pom.model` value in `selenide-pom.properties` if defined, or [DEFAULT_MODEL] in another case.
     */
    var model: String
        get() = threadLocalModel.get()
        set(value) {
            logger.debug { "New value for SPConfig.model: $value" }
            threadLocalModel.set(value)
        }

    private val threadLocalLang = ThreadLocal.withInitial {
        val default = fileProperties.getProperty("selenide-pom.lang", DEFAULT_LANG)
        val initial = System.getProperty("selenide-pom.lang", default)
        logger.debug { "Initial value for SPConfig.lang: $initial" }
        initial
    }

    /**
     * The default `lang` (thread local value) used in some methods in
     * [com.github.qky666.selenidepom.pom.LangConditioned] and [com.github.qky666.selenidepom.pom.Loadable].
     * Default value: `selenide-pom.lang` System property if defined, `selenide-pom.lang` value
     * in `selenide-pom.properties` if defined, or [DEFAULT_LANG] in another case.
     */
    var lang: String
        get() = threadLocalLang.get()
        set(value) {
            logger.debug { "New value for SPConfig.lang: $value" }
            threadLocalLang.set(value)
        }

    private val threadLocalSelenideConfig = ThreadLocal.withInitial { SelenideConfig() }

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
        model: String = DEFAULT_DESKTOP_MODEL,
    ) {
        selenideConfig.browser(browser)
        SPConfig.model = model
    }

    /**
     * Sets up a basic mobile browser on thread local [selenideConfig] configuration using the provided [deviceName]
     * to add mobile emulation and sets the default [model] to use.
     *
     * @param deviceName The name of the emulated mobile device
     * @param model The default `model` to use
     */
    @JvmOverloads
    fun setupBasicMobileBrowser(deviceName: String = DEFAULT_DEVICE_NAME, model: String = DEFAULT_MOBILE_MODEL) {
        ChromeMobileDriverFactory.deviceName = deviceName
        selenideConfig.browser(ChromeMobileDriverFactory::class.qualifiedName ?: "null")
        SPConfig.model = model
    }

    /**
     * Creates a new [Driver] instance based on thread local [selenideConfig] configuration and returns it.
     *
     * @return created [Driver] instance
     */
    fun createDriver() = SelenideDriver(selenideConfig).driver()

    /**
     * Sets [newDriver] as the current thread [Driver] and returns it.
     * If [newDriver] is `null`, a new [Driver] instance is created using current thread local
     * [selenideConfig] configuration.
     *
     * @return [Driver] instance
     */
    @JvmOverloads
    @Synchronized
    fun setDriver(newDriver: Driver? = null): Driver {
        val driver = newDriver ?: createDriver()
        val webDriver = driver.getAndCheckWebDriver()
        setWebDriver(webDriver)
        return driver
    }

    /**
     * Sets [newDriver] as the current thread [WebDriver] and returns it.
     *
     * @return [WebDriver] the same [newDriver] object
     */
    fun setWebDriver(newDriver: WebDriver): WebDriver {
        WebDriverRunner.setWebDriver(newDriver)
        return newDriver
    }

    /**
     * Gets the current thread [WebDriver] and returns it if exists, null if not.
     *
     * @return Current thread [WebDriver] or null if it does not exist
     */
    fun getWebDriver() = try {
        WebDriverRunner.getWebDriver()
    } catch (_: IllegalStateException) {
        null
    }

    /**
     * Quits the current thread [WebDriver], closing its windows first to avoid problems.
     *
     * Note: Sometimes Selenide.closeWebDriver() does not close the WebDriver correctly (possible WebDriver bug).
     * Closing every window first is safer.
     */
    fun quitDriver() {
        getWebDriver()?.let {
            while (it.windowHandles.size > 1) {
                it.switchTo().window(it.windowHandles.first())
                it.close()
            }
            if (it.windowHandles.isNotEmpty()) {
                it.switchTo().window(it.windowHandles.first())
                it.close()
            }
        }
        Selenide.closeWebDriver()
    }

    /**
     * Returns current thread Selenide timeout as a [Duration] object.
     *
     * @return current thread Selenide timeout
     */
    fun timeout(): Duration = Duration.ofMillis(selenideConfig.timeout())

    /**
     * Returns a [LocalDateTime] object that represents the moment when current thread Selenide timeout is reached
     * (starting from now).
     *
     * @return moment when current thread Selenide timeout is reached
     */
    fun timeoutFromNow(): LocalDateTime = LocalDateTime.now() + timeout()
}
