package com.github.qky666.selenidepom.test.kotlin.mtp

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide.closeWebDriver
import com.codeborne.selenide.Selenide.open
import com.codeborne.selenide.ex.ElementNotFound
import com.github.qky666.selenidepom.error.RequiredError
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.hasLoadedRequired
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.mainFramePage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.servicesPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.servicesRequiredErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.servicesShouldLoadRequiredErrorPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration

class MtpKotlinTest {

    private var testData = TestData("prod")

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
        if (browserConfig.equals("chromeMobile", ignoreCase = true)) {
            SPConfig.setupBasicMobileBrowser()
        } else {
            SPConfig.setupBasicDesktopBrowser(browserConfig)
        }
        // testData is already initialized, but if there were more environments this could be a good place to
        // set testData
        testData.resetData("prod")
        open(testData.input.getProperty("data.input.baseUrl"))
        // Additional test for output in TestData
        testData.output["threadId"] = Thread.currentThread().id
    }

    @AfterEach
    fun closeBrowser() {
        closeWebDriver()
        // Additional test for output in TestData
        Assertions.assertEquals(testData.output["threadId"].toString(), Thread.currentThread().id.toString())
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mainMenu.services.hover()
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click()
        servicesPage.shouldLoadRequired()
        Assertions.assertEquals(
            "div#cookie-law-info-bar/a#cookie_action_close_header", servicesPage.cookiesBanner.accept.searchCriteria
        )
        // Cookies message should not reappear
        servicesPage.cookiesBanner.shouldNotBe(visible)
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun badSelectorErrorDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mainMenu.services.hover()
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click()
        servicesPage.shouldLoadRequired()
        Assertions.assertThrows(ElementNotFound::class.java) { servicesPage.badSelector.click() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mainMenu.services.hover()
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.shouldBe(visible).click()
        Assertions.assertThrows(RequiredError::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithBadSelectorRequiredDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
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
        mainFramePage.acceptCookies()
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
        mainFramePage.acceptCookies()
        mainFramePage.mobileMenuButton.click()
        val mobileMenu = mainFramePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services.click()
        mobileMenu.servicesQualityAssurance.shouldBe(visible).click()
        servicesPage.shouldLoadRequired()
        Assertions.assertEquals(
            "div#cookie-law-info-bar/a#cookie_action_close_header", servicesPage.cookiesBanner.accept.searchCriteria
        )
        // Cookies message should not reappear
        servicesPage.cookiesBanner.shouldNotBe(visible)
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun badSelectorErrorMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mobileMenuButton.click()
        val mobileMenu = mainFramePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services.click()
        mobileMenu.servicesQualityAssurance.shouldBe(visible).click()
        servicesPage.shouldLoadRequired()
        Assertions.assertThrows(ElementNotFound::class.java) { servicesPage.badSelector.click() }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mobileMenuButton.click()
        val mobileMenu = mainFramePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services.click()
        mobileMenu.servicesQualityAssurance.shouldBe(visible).click()
        Assertions.assertThrows(RequiredError::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithBadSelectorRequiredMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mobileMenuButton.click()
        val mobileMenu = mainFramePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services.click()
        mobileMenu.servicesQualityAssurance.shouldBe(visible).click()
        val error =
            Assertions.assertThrows(RequiredError::class.java) { servicesRequiredErrorPage.shouldLoadRequired() }
        Assertions.assertEquals(2, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userForgotClickMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mobileMenuButton.click()
        val mobileMenu = mainFramePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services.click()
        // User forgot to click Quality Assurance link
        Assertions.assertFalse(servicesPage.hasLoadedRequired())
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)))
        Assertions.assertThrows(RequiredError::class.java) { servicesPage.shouldLoadRequired() }
        Assertions.assertThrows(RequiredError::class.java) { servicesPage.shouldLoadRequired(Duration.ofMillis(100)) }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceDesktopWrongPomVersion(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mobileMenuButton.click()
        val mobileMenu = mainFramePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services.click()
        mobileMenu.servicesQualityAssurance.shouldBe(visible).click()
        Assertions.assertThrows(RequiredError::class.java) { servicesPage.shouldLoadRequired("desktop") }
        Assertions.assertFalse(servicesPage.hasLoadedRequired("desktop"))
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100), "desktop"))
    }
}
