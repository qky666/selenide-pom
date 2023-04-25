package com.github.qky666.selenidepom.test.kotlin

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import com.github.qky666.selenidepom.config.SPConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver

class SPConfigTest {

    @BeforeEach
    fun beforeEach() {
        SPConfig.resetConfig()
        // Test SPConfig initial values
        Assertions.assertEquals(SPConfig.lang, "es")
        Assertions.assertEquals(SPConfig.model, "fileModel")
    }

    @AfterEach
    fun afterEach() {
        SPConfig.quitCurrentThreadDriver()
    }

    @Test
    fun mobileEmulationTest() {
        SPConfig.setupBasicMobileBrowser()
        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals(
            "chrome",
            SPConfig.selenideConfig.browserCapabilities().getCapability("browserName")
        )
        Assertions.assertEquals(
            "{args=[--remote-allow-origins=*], extensions=[], mobileEmulation={deviceName=${SPConfig.DEFAULT_DEVICE_NAME}}}",
            SPConfig.selenideConfig.browserCapabilities().getCapability("goog:chromeOptions").toString()
        )
    }

    @Test
    fun mobileEmulationCustomTest() {
        val deviceName = "Nexus 4"
        SPConfig.setupBasicMobileBrowser(deviceName)
        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals(
            "chrome",
            SPConfig.selenideConfig.browserCapabilities().getCapability("browserName")
        )
        Assertions.assertEquals(
            "{args=[--remote-allow-origins=*], extensions=[], mobileEmulation={deviceName=$deviceName}}",
            SPConfig.selenideConfig.browserCapabilities().getCapability("goog:chromeOptions").toString()
        )
    }

    @Test
    fun setDriverTest() {
        val browser = "firefox"
        SPConfig.selenideConfig.browser(browser)
        val createdDriver = SPConfig.createDriver()
        WebDriverRunner.setWebDriver(createdDriver.getAndCheckWebDriver())

        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(FirefoxDriver::class.java, driver)
        Assertions.assertEquals(createdDriver.webDriver, driver)
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
        val createdDriver = SPConfig.setCurrentThreadDriver()
        val driver = WebDriverRunner.getWebDriver()
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

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(
            "{args=[--remote-allow-origins=*], extensions=[], mobileEmulation={deviceName=${SPConfig.DEFAULT_DEVICE_NAME}}}",
            SPConfig.selenideConfig.browserCapabilities().getCapability("goog:chromeOptions").toString()
        )
        Assertions.assertEquals(SPConfig.DEFAULT_MOBILE_MODEL, SPConfig.model)

        val createdDriver = SPConfig.setCurrentThreadDriver()
        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
        Assertions.assertEquals(createdDriver.webDriver, driver)
    }

    @Test
    fun basicMobileBrowserCustomTest() {
        val deviceName = "Nexus 4"
        val model = "MyModel"
        SPConfig.setupBasicMobileBrowser(deviceName, model)

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(
            "{args=[--remote-allow-origins=*], extensions=[], mobileEmulation={deviceName=$deviceName}}",
            SPConfig.selenideConfig.browserCapabilities().getCapability("goog:chromeOptions").toString()
        )
        Assertions.assertEquals(model, SPConfig.model)

        val createdDriver = SPConfig.setCurrentThreadDriver()
        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
        Assertions.assertEquals(createdDriver.webDriver, driver)
    }

    @Test
    fun basicDesktopBrowserTest() {
        SPConfig.setupBasicDesktopBrowser()

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals(null, SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(SPConfig.DEFAULT_DESKTOP_MODEL, SPConfig.model)

        val createdDriver = SPConfig.setCurrentThreadDriver()
        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
        Assertions.assertEquals(createdDriver.webDriver, driver)
    }

    @Test
    fun basicDesktopBrowserCustomTest() {
        val browser = "firefox"
        val model = "myModel"
        SPConfig.setupBasicDesktopBrowser(browser, model)

        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(null, SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(model, SPConfig.model)

        val createdDriver = SPConfig.setCurrentThreadDriver()
        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(FirefoxDriver::class.java, driver)
        Assertions.assertEquals(createdDriver.webDriver, driver)
    }

    @Test
    fun illegalWebDriverTest() {
        SPConfig.setCurrentThreadDriver()
        Selenide.closeWebDriver()
        Assertions.assertNull(SPConfig.getCurrentWebDriver())
    }
}
