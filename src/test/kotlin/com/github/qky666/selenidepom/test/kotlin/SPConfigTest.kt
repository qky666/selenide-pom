package com.github.qky666.selenidepom.test.kotlin

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import com.github.qky666.selenidepom.config.SPConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver

class SPConfigTest {
    @Test
    fun addMobileEmulationTest() {
        SPConfig.resetSelenideConfig()
        SPConfig.addMobileEmulation()

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(
            "{args=[], extensions=[], mobileEmulation={deviceName=${SPConfig.defaultDeviceName}}}",
            SPConfig.selenideConfig.browserCapabilities().getCapability("goog:chromeOptions").toString()
        )
    }

    @Test
    fun addMobileEmulationCustomTest() {
        val deviceName = "My device name"
        SPConfig.resetSelenideConfig()
        SPConfig.addMobileEmulation(deviceName)

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(
            "{args=[], extensions=[], mobileEmulation={deviceName=$deviceName}}",
            SPConfig.selenideConfig.browserCapabilities().getCapability("goog:chromeOptions").toString()
        )
    }

    @Test
    fun setWebDriverTest() {
        val browser = "firefox"
        SPConfig.resetSelenideConfig()
        SPConfig.selenideConfig.browser(browser)
        SPConfig.setWebDriver()

        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(FirefoxDriver::class.java, driver)
    }

    @Test
    fun resetSelenideConfigTest() {
        val browser = "firefox"
        val model = "MyModel"
        SPConfig.resetSelenideConfig()
        SPConfig.selenideConfig.browser(browser)
        SPConfig.model = model
        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(model, SPConfig.model)
        SPConfig.resetSelenideConfig()

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("fileModel", SPConfig.model)
        Selenide.open()
        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
    }

    @Test
    fun setupBasicMobileBrowserTest() {
        SPConfig.setupBasicMobileBrowser()

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(
            "{args=[], extensions=[], mobileEmulation={deviceName=${SPConfig.defaultDeviceName}}}",
            SPConfig.selenideConfig.browserCapabilities().getCapability("goog:chromeOptions").toString()
        )
        Assertions.assertEquals(SPConfig.defaultMobileModel, SPConfig.model)

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
    }

    @Test
    fun setupBasicMobileBrowserCustomTest() {
        val deviceName = "Nexus 4"
        val model = "MyModel"
        SPConfig.setupBasicMobileBrowser(deviceName, model)

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(
            "{args=[], extensions=[], mobileEmulation={deviceName=$deviceName}}",
            SPConfig.selenideConfig.browserCapabilities().getCapability("goog:chromeOptions").toString()
        )
        Assertions.assertEquals(model, SPConfig.model)

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
    }

    @Test
    fun setupBasicDesktopBrowserTest() {
        SPConfig.setupBasicDesktopBrowser()

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals(null, SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(SPConfig.defaultDesktopModel, SPConfig.model)

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
    }

    @Test
    fun setupBasicDesktopBrowserCustomTest() {
        val browser = "firefox"
        val model = "myModel"
        SPConfig.setupBasicDesktopBrowser(browser, model)

        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(null, SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(model, SPConfig.model)

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(FirefoxDriver::class.java, driver)
    }

    @AfterEach
    fun afterEach() {
        Selenide.closeWebDriver()
    }
}
