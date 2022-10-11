package com.github.qky666.selenidepom.test.kotlin.mtp

import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.ex.ElementNotFound
import com.github.qky666.selenidepom.pom.RequiredError
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.hasLoadedRequired
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.mainFramePage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresult.searchResultsCollectionErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresult.searchResultsPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresult.searchResultsErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.servicesPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.servicesRequiredErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.servicesShouldLoadRequiredErrorPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration

class MtpKotlinTest {

    private val testData = TestData("prod")

    private val searchResultsExpected = 8
    private val searchString = "Mexico"


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
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
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
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        servicesPage.shouldLoadRequired()
        val badSelector = element("bad-selector")
        Assertions.assertThrows(ElementNotFound::class.java) { badSelector.click() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mainMenu.services.hover()
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        Assertions.assertThrows(RequiredError::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithBadSelectorRequiredDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mainMenu.services.hover()
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
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
        val badSelector = element("bad-selector")
        Assertions.assertThrows(ElementNotFound::class.java) { badSelector.click() }
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

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun search(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mainMenu.searchOpen.click()
        mainFramePage.mainMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys(searchString)
        mainFramePage.mainMenu.searchMenu.doSearch.click()
        mainFramePage.mainMenu.searchMenu.should(disappear)

        searchResultsPage.shouldLoadRequired().breadcrumb.activeBreadcrumbItem.shouldHave(exactText("Results: $searchString"))
        searchResultsPage.shouldLoadRequired().breadcrumb.breadcrumbItems[0].shouldHave(exactText("Home"))
        Assertions.assertEquals(searchResultsExpected, searchResultsPage.searchResults.shouldLoadRequired().count())
        val mtp25 = searchResultsPage.searchResults.filterBy(text("MTP, 25 años como empresa de referencia"))
            .shouldHave(size(1))[0]
        mtp25.title.shouldHave(exactText("MTP, 25 años como empresa de referencia en aseguramiento de negocios digitales"))
        mtp25.text.shouldHave(text("MTP es hoy una empresa de referencia en Digital Business Assurance"))
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun searchRequiredError(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mainMenu.searchOpen.click()
        mainFramePage.mainMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys(searchString)
        mainFramePage.mainMenu.searchMenu.doSearch.click()
        mainFramePage.mainMenu.searchMenu.should(disappear)

        val error =
            Assertions.assertThrows(RequiredError::class.java) { searchResultsErrorPage.shouldLoadRequired().searchResultsError.shouldLoadRequired() }
        Assertions.assertEquals(searchResultsExpected, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun searchCollectionError(browserConfig: String) {
        setUpBrowser(browserConfig)
        mainFramePage.acceptCookies()
        mainFramePage.mainMenu.searchOpen.click()
        mainFramePage.mainMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys(searchString)
        mainFramePage.mainMenu.searchMenu.doSearch.click()
        mainFramePage.mainMenu.searchMenu.should(disappear)

        val error = Assertions.assertThrows(RequiredError::class.java) { searchResultsCollectionErrorPage.shouldLoadRequired().searchResultsError.shouldLoadRequired() }
        Assertions.assertEquals(1, error.suppressed.size)
    }
}
