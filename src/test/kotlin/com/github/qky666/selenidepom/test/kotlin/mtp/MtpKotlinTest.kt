package com.github.qky666.selenidepom.test.kotlin.mtp

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import com.codeborne.selenide.ex.ElementNotFound
import com.codeborne.selenide.ex.ElementShould
import com.github.qky666.selenidepom.RequiredError
import com.github.qky666.selenidepom.SPConfig
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.MainFramePage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.ServicesPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.ServicesRequiredErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.ServicesShouldLoadRequiredErrorPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.openqa.selenium.MutableCapabilities
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration

class MtpKotlinTest {
    val mainFramePage = MainFramePage()
    val servicesPage = ServicesPage()
    val servicesRequiredErrorPage = ServicesRequiredErrorPage()
    val servicesShouldLoadRequiredErrorPage = ServicesShouldLoadRequiredErrorPage()

    companion object {
        @JvmStatic
        fun desktopBrowserConfigSource(): List<String> {
            return listOf("chrome", "firefox")
        }

        @JvmStatic
        fun mobileBrowserConfigSource(): List<String> {
            return listOf("chromeMobile")
        }
    }

    private fun setUpBrowser(browserConfig: String) {
        if (browserConfig.equals("chrome", ignoreCase = true)) {
            setUpChrome()
        } else if (browserConfig.equals("firefox", ignoreCase = true)) {
            setUpFirefox()
        } else if (browserConfig.equals("chromeMobile", ignoreCase = true)) {
            setUpChromeMobile()
        } else {
            throw RuntimeException("Unknown browserConfig: $browserConfig")
        }
        val selenideConfig = SPConfig.getSelenideConfig()
        selenideConfig.baseUrl("https://mtp.es")

        WebDriverRunner.setWebDriver(SPConfig.webDriverFactory.createWebDriver(selenideConfig, null, null))
        Selenide.open(selenideConfig.baseUrl())
    }

    private fun setUpChrome() {
        val selenideConfig = SPConfig.getSelenideConfig()
        selenideConfig.browser("chrome")
        selenideConfig.browserCapabilities(MutableCapabilities())
        SPConfig.setPomVersion("desktop")
    }

    private fun setUpFirefox() {
        val selenideConfig = SPConfig.getSelenideConfig()
        selenideConfig.browser("firefox")
        selenideConfig.browserCapabilities(MutableCapabilities())
        SPConfig.setPomVersion("desktop")
    }

    private fun setUpChromeMobile() {
        val selenideConfig = SPConfig.getSelenideConfig()
        selenideConfig.browser("chrome")
        val chromeOptions = ChromeOptions()
        chromeOptions.setExperimentalOption("mobileEmulation", mapOf("deviceName" to "Nexus 5"))
        val newCapabilities = chromeOptions.merge(selenideConfig.browserCapabilities())
        selenideConfig.browserCapabilities(newCapabilities)
        SPConfig.setPomVersion("mobile")
    }

    fun acceptCookies() {
        mainFramePage.shouldLoadRequired()
        mainFramePage.cookiesBanner.acceptCookies()
    }

    @AfterEach
    fun closeBrowser() {
        Selenide.closeWebDriver()
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mainMenu.services.hover()
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click()
        servicesPage.shouldLoadRequired()
        Assertions.assertEquals(
            "div#cookie-law-info-bar/a#cookie_action_close_header", servicesPage.cookiesBanner.accept.searchCriteria
        )
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun badSelectorErrorDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mainMenu.services.hover()
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click()
        servicesPage.shouldLoadRequired()
        Assertions.assertThrows(ElementNotFound::class.java) { servicesPage.badSelector.click() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mainMenu.services.hover()
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click()
        Assertions.assertThrows(ElementShould::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithBadSelectorRequiredDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mainMenu.services.hover()
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click()
        val error =
            Assertions.assertThrows(RequiredError::class.java) { servicesRequiredErrorPage.shouldLoadRequired() }
        Assertions.assertEquals(2, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userForgotClickDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mainMenu.services.hover()
        // User forgot to click Quality Assurance link
        Assertions.assertFalse(servicesPage.hasLoadedRequired())
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)))
        Assertions.assertThrows(RequiredError::class.java) { servicesPage.shouldLoadRequired() }
        Assertions.assertThrows(RequiredError::class.java) { servicesPage.shouldLoadRequired(Duration.ofMillis(100)) }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        mainFramePage.mobileMenu.servicesQualityAssurance.click()
        servicesPage.shouldLoadRequired()
        Assertions.assertEquals(
            "div#cookie-law-info-bar/a#cookie_action_close_header", servicesPage.cookiesBanner.accept.searchCriteria
        )
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun badSelectorErrorMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        mainFramePage.mobileMenu.servicesQualityAssurance.click()
        servicesPage.shouldLoadRequired()
        Assertions.assertThrows(ElementNotFound::class.java) { servicesPage.badSelector.click() }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        mainFramePage.mobileMenu.servicesQualityAssurance.click()
        Assertions.assertThrows(ElementShould::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithBadSelectorRequiredMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        mainFramePage.mobileMenu.servicesQualityAssurance.click()
        val error =
            Assertions.assertThrows(RequiredError::class.java) { servicesRequiredErrorPage.shouldLoadRequired() }
        Assertions.assertEquals(2, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userForgotClickMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        acceptCookies()
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        // User forgot to click Quality Assurance link
        Assertions.assertFalse(servicesPage.hasLoadedRequired())
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)))
        Assertions.assertThrows(RequiredError::class.java) { servicesPage.shouldLoadRequired() }
        Assertions.assertThrows(RequiredError::class.java) { servicesPage.shouldLoadRequired(Duration.ofMillis(100)) }
    }
}