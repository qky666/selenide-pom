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
            "{args=[], extensions=[], mobileEmulation={deviceName=${deviceName}}}",
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
        val pomVersion = "MyPomVersion"
        SPConfig.resetSelenideConfig()
        SPConfig.selenideConfig.browser(browser)
        SPConfig.pomVersion = pomVersion
        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(pomVersion, SPConfig.pomVersion)
        SPConfig.resetSelenideConfig()

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("filePomVersion", SPConfig.pomVersion)
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
        Assertions.assertEquals(SPConfig.defaultMobilePomVersion, SPConfig.pomVersion)

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
    }

    @Test
    fun setupBasicMobileBrowserCustomTest() {
        val deviceName = "Nexus 4"
        val pomVersion = "MyPomVersion"
        SPConfig.setupBasicMobileBrowser(deviceName, pomVersion)

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(
            "{args=[], extensions=[], mobileEmulation={deviceName=$deviceName}}",
            SPConfig.selenideConfig.browserCapabilities().getCapability("goog:chromeOptions").toString()
        )
        Assertions.assertEquals(pomVersion, SPConfig.pomVersion)

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
    }

    @Test
    fun setupBasicDesktopBrowserTest() {
        SPConfig.setupBasicDesktopBrowser()

        Assertions.assertEquals("chrome", SPConfig.selenideConfig.browser())
        Assertions.assertEquals(null, SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(SPConfig.defaultDesktopPomVersion, SPConfig.pomVersion)

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(ChromeDriver::class.java, driver)
    }

    @Test
    fun setupBasicDesktopBrowserCustomTest() {
        val browser = "firefox"
        val pomVersion = "myPomVersion"
        SPConfig.setupBasicDesktopBrowser(browser, pomVersion)

        Assertions.assertEquals(browser, SPConfig.selenideConfig.browser())
        Assertions.assertEquals(null, SPConfig.selenideConfig.browserCapabilities().getCapability("browserName"))
        Assertions.assertEquals(pomVersion, SPConfig.pomVersion)

        val driver = WebDriverRunner.getWebDriver()
        Assertions.assertInstanceOf(FirefoxDriver::class.java, driver)
    }

    @AfterEach
    fun afterEach() {
        Selenide.closeWebDriver()
    }
}
