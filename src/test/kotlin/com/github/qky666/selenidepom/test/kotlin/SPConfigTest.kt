package com.github.qky666.selenidepom.test.kotlin

import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.ChromeMobileDriverFactory
import com.github.qky666.selenidepom.config.DEFAULT_DESKTOP_MODEL
import com.github.qky666.selenidepom.config.DEFAULT_MOBILE_MODEL
import com.github.qky666.selenidepom.config.SPConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import java.time.Duration
import java.time.LocalDateTime
import javax.imageio.ImageIO
import kotlin.test.assertTrue

class SPConfigTest {

    private val logger = KotlinLogging.logger {}

    @BeforeEach
    fun beforeEach() {
        SPConfig.resetConfig()
        // Test SPConfig initial values
        Assertions.assertEquals(SPConfig.lang, "spa")
        Assertions.assertEquals(SPConfig.model, "fileModel")
    }

    @AfterEach
    fun afterEach() {
        SPConfig.quitDriver()
    }

    @Test
    fun mobileEmulationTest() {
        SPConfig.setupBasicMobileBrowser()
        val driver = SPConfig.setDriver()
        Selenide.open("https://google.es")
        val browserName = driver.config().browserCapabilities().getCapability("browserName")
        Assertions.assertEquals(ChromeMobileDriverFactory::class.qualifiedName, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(ChromeMobileDriverFactory::class.qualifiedName, driver.browser().name)
        Assertions.assertNull(browserName)
        Assertions.assertNull(driver.config().browserCapabilities().getCapability("goog:chromeOptions"))
        Assertions.assertFalse(driver.browser().isChrome)
        val image = Selenide.screenshot(OutputType.BYTES)!!.inputStream().use { ImageIO.read(it) }
        logger.info { "Size: ${image.width}x${image.height}" }
        Assertions.assertEquals(1080, image.width)
        Assertions.assertEquals(1920, image.height)
    }

    @Test
    fun mobileEmulationCustomTest() {
        val deviceName = "Nexus 4"
        SPConfig.setupBasicMobileBrowser(deviceName)
        val driver = SPConfig.setDriver()
        Selenide.open("https://google.es")
        val browserName = driver.config().browserCapabilities().getCapability("browserName")
        Assertions.assertEquals(ChromeMobileDriverFactory::class.qualifiedName, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(ChromeMobileDriverFactory::class.qualifiedName, driver.browser().name)
        Assertions.assertNull(browserName)
        Assertions.assertNull(driver.config().browserCapabilities().getCapability("goog:chromeOptions"))
        Assertions.assertFalse(driver.browser().isChrome)
        val image = Selenide.screenshot(OutputType.BYTES)!!.inputStream().use { ImageIO.read(it) }
        logger.info { "Size: ${image.width}x${image.height}" }
        Assertions.assertEquals(768, image.width)
        Assertions.assertEquals(1280, image.height)
    }

    @Test
    fun setDriverTest() {
        val browser = "firefox"
        SPConfig.selenideConfig.browser(browser)
        val createdDriver = SPConfig.createDriver()
        SPConfig.setDriver(createdDriver)

        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())

        val driver = SPConfig.getWebDriver()
        Assertions.assertInstanceOf(FirefoxDriver::class.java, driver)
        Assertions.assertEquals(createdDriver.webDriver, driver)
    }

    @Test
    fun setWebDriverTest() {
        val browser = "firefox"
        SPConfig.selenideConfig.browser(browser)
        val createdDriver = SPConfig.createDriver().getAndCheckWebDriver()
        SPConfig.setWebDriver(createdDriver)

        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())

        val driver = SPConfig.getWebDriver()
        Assertions.assertInstanceOf(FirefoxDriver::class.java, driver)
        Assertions.assertEquals(createdDriver, driver)
    }

    @Test
    fun resetConfigTest() {
        val browser = "firefox"
        val model = "MyModel"
        SPConfig.selenideConfig.browser(browser)
        SPConfig.model = model
        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(model, SPConfig.model)
        SPConfig.resetConfig()

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("fileModel", SPConfig.model)
        val createdDriver = SPConfig.setDriver()
        val driver = SPConfig.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
        Assertions.assertEquals(createdDriver.webDriver, driver)
    }

    @Test
    fun systemPropertiesConfigTest() {
        val lang = "MyLang"
        val model = "MyModel"
        System.setProperty("selenide-pom.model", model)
        System.setProperty("selenide-pom.lang", lang)
        SPConfig.resetConfig()
        Assertions.assertEquals(model, SPConfig.model)
        Assertions.assertEquals(lang, SPConfig.lang)
        System.clearProperty("selenide-pom.model")
        System.clearProperty("selenide-pom.lang")
    }

    @Test
    fun basicMobileBrowserTest() {
        SPConfig.setupBasicMobileBrowser()
        val driver = SPConfig.setDriver()
        Selenide.open("https://google.es")
        val browserName = driver.config().browserCapabilities().getCapability("browserName")
        Assertions.assertEquals(ChromeMobileDriverFactory::class.qualifiedName, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(ChromeMobileDriverFactory::class.qualifiedName, driver.browser().name)
        Assertions.assertNull(browserName)
        Assertions.assertNull(driver.config().browserCapabilities().getCapability("goog:chromeOptions"))
        Assertions.assertFalse(driver.browser().isChrome)
        val image = Selenide.screenshot(OutputType.BYTES)!!.inputStream().use { ImageIO.read(it) }
        logger.info { "Size: ${image.width}x${image.height}" }
        Assertions.assertEquals(1080, image.width)
        Assertions.assertEquals(1920, image.height)
        Assertions.assertEquals(DEFAULT_MOBILE_MODEL, SPConfig.model)
        val webDriver = SPConfig.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, webDriver)
        Assertions.assertEquals(driver.webDriver, webDriver)
    }

    @Test
    fun basicMobileBrowserCustomTest() {
        val deviceName = "Nexus 4"
        val model = "MyModel"
        SPConfig.setupBasicMobileBrowser(deviceName, model)
        val driver = SPConfig.setDriver()
        Selenide.open("https://google.es")
        val browserName = driver.config().browserCapabilities().getCapability("browserName")
        Assertions.assertEquals(ChromeMobileDriverFactory::class.qualifiedName, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(ChromeMobileDriverFactory::class.qualifiedName, driver.browser().name)
        Assertions.assertNull(browserName)
        Assertions.assertNull(driver.config().browserCapabilities().getCapability("goog:chromeOptions"))
        Assertions.assertFalse(driver.browser().isChrome)
        val image = Selenide.screenshot(OutputType.BYTES)!!.inputStream().use { ImageIO.read(it) }
        logger.info { "Size: ${image.width}x${image.height}" }
        Assertions.assertEquals(768, image.width)
        Assertions.assertEquals(1280, image.height)
        Assertions.assertEquals(model, SPConfig.model)
        val webDriver = SPConfig.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, webDriver)
        Assertions.assertEquals(driver.webDriver, webDriver)
    }

    @Test
    fun basicDesktopBrowserTest() {
        SPConfig.setupBasicDesktopBrowser()

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        val browserName = SPConfig.selenideConfig.browserCapabilities().getCapability("browserName")
        Assertions.assertEquals(null, browserName)
        Assertions.assertEquals(DEFAULT_DESKTOP_MODEL, SPConfig.model)

        val createdDriver = SPConfig.setDriver()
        val driver = SPConfig.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
        Assertions.assertEquals(createdDriver.webDriver, driver)
    }

    @Test
    fun basicDesktopBrowserCustomTest() {
        val browser = "firefox"
        val model = "myModel"
        SPConfig.setupBasicDesktopBrowser(browser, model)

        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())
        val browserName = SPConfig.selenideConfig.browserCapabilities().getCapability("browserName")
        Assertions.assertEquals(null, browserName)
        Assertions.assertEquals(model, SPConfig.model)

        val createdDriver = SPConfig.setDriver()
        val driver = SPConfig.getWebDriver()
        Assertions.assertInstanceOf(FirefoxDriver::class.java, driver)
        Assertions.assertEquals(createdDriver.webDriver, driver)
    }

    @Test
    fun illegalWebDriverStateTest() {
        SPConfig.setDriver()
        Selenide.closeWebDriver()
        Assertions.assertNull(SPConfig.getWebDriver())
    }

    @Test
    fun timeoutFromNowTest() {
        val now = LocalDateTime.now()
        val timeoutFromNow = SPConfig.timeoutFromNow()
        val delta = Duration.ofMillis(100)
        assertTrue { now <= timeoutFromNow }
        assertTrue { now + SPConfig.timeout() <= timeoutFromNow }
        assertTrue { now + Duration.ofMillis(SPConfig.selenideConfig.timeout()) <= timeoutFromNow }
        assertTrue { now + SPConfig.timeout() + delta > timeoutFromNow }
    }
}
