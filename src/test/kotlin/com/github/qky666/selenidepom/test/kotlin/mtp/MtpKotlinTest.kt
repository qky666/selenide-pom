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
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.home.homePage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services.qualityAssurancePage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults.searchResultsCollectionErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults.searchResultsPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults.searchResultsErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services.servicesPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services.servicesRequiredErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services.servicesShouldLoadRequiredErrorPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration

class MtpKotlinTest {

    private val testData = TestData("prod")

    private val maxResultsPerPageExpected = 5

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
        SPConfig.lang = "es"
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
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mainMenu.services.hover()
        homePage.mainMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        qualityAssurancePage.shouldLoadRequired()
        Assertions.assertEquals(
            "div#cookie-law-info-bar/a#cookie_action_close_header",
            qualityAssurancePage.cookiesBanner.accept.searchCriteria
        )
        // Cookies message should not reappear
        qualityAssurancePage.cookiesBanner.shouldNotBe(visible)
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun badSelectorErrorDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mainMenu.services.hover()
        homePage.mainMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        qualityAssurancePage.shouldLoadRequired()
        val badSelector = element("bad-selector")
        Assertions.assertThrows(ElementNotFound::class.java) { badSelector.click() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mainMenu.services.hover()
        homePage.mainMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        Assertions.assertThrows(RequiredError::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithBadSelectorRequiredDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mainMenu.services.hover()
        homePage.mainMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        val error =
            Assertions.assertThrows(RequiredError::class.java) { servicesRequiredErrorPage.shouldLoadRequired() }
        Assertions.assertEquals(2, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun userForgotClickDesktop(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mainMenu.services.hover()
        // User forgot to click Quality Assurance link
        Assertions.assertFalse(qualityAssurancePage.hasLoadedRequired())
        Assertions.assertFalse(qualityAssurancePage.hasLoadedRequired(Duration.ofMillis(100)))
        Assertions.assertThrows(RequiredError::class.java) { qualityAssurancePage.shouldLoadRequired() }
        Assertions.assertThrows(RequiredError::class.java) {
            qualityAssurancePage.shouldLoadRequired(
                Duration.ofMillis(
                    100
                )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        qualityAssurancePage.shouldLoadRequired()
        Assertions.assertEquals(
            "div#cookie-law-info-bar/a#cookie_action_close_header",
            qualityAssurancePage.cookiesBanner.accept.searchCriteria
        )
        // Cookies message should not reappear
        qualityAssurancePage.cookiesBanner.shouldNotBe(visible)
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun badSelectorErrorMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        qualityAssurancePage.shouldLoadRequired()
        val badSelector = element("bad-selector")
        Assertions.assertThrows(ElementNotFound::class.java) { badSelector.click() }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        Assertions.assertThrows(RequiredError::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceWithBadSelectorRequiredMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        val error =
            Assertions.assertThrows(RequiredError::class.java) { servicesRequiredErrorPage.shouldLoadRequired() }
        Assertions.assertEquals(2, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userForgotClickMobile(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        // User forgot to click Quality Assurance link
        Assertions.assertFalse(qualityAssurancePage.hasLoadedRequired())
        Assertions.assertFalse(qualityAssurancePage.hasLoadedRequired(Duration.ofMillis(100)))
        Assertions.assertThrows(RequiredError::class.java) { qualityAssurancePage.shouldLoadRequired() }
        Assertions.assertThrows(RequiredError::class.java) { qualityAssurancePage.shouldLoadRequired(Duration.ofMillis(100)) }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    fun userNavigateToQualityAssuranceDesktopWrongPomVersion(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenu
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        Assertions.assertThrows(RequiredError::class.java) { qualityAssurancePage.shouldLoadRequired("desktop") }
        Assertions.assertFalse(qualityAssurancePage.hasLoadedRequired("desktop"))
        Assertions.assertFalse(qualityAssurancePage.hasLoadedRequired(timeout = Duration.ofMillis(100), lang = "desktop"))
    }

    @ParameterizedTest
    @CsvSource(
        "chrome,Mexico,2,3,'MTP, 25 años como empresa de referencia en aseguramiento de negocios digitales','MTP es hoy una empresa de referencia en Digital Business Assurance'",
        "firefox,Mexico,2,3,'MTP, 25 años como empresa de referencia en aseguramiento de negocios digitales','MTP es hoy una empresa de referencia en Digital Business Assurance'",
        "chrome,Viajero,2,2,'Los valores MTP, claves para este 2020','Este año 2020 ha sido un año particular y totalmente atípico para todos'",
        "firefox,Viajero,2,2,'Los valores MTP, claves para este 2020','Este año 2020 ha sido un año particular y totalmente atípico para todos'"
    )
    fun searchMexico(
        browserConfig: String,
        searchString: String,
        resultsPagesExpected: Int,
        lastPageResultsExpected: Int,
        lastPageResultTitle: String,
        lastPageResultText: String
    ) {

        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.shouldLoadRequired().mainBanner.verifyTextsEs()
        homePage.mainMenu.searchOpen.click()
        homePage.mainMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys(searchString)
        homePage.mainMenu.searchMenu.doSearch.click()
        homePage.mainMenu.searchMenu.should(disappear)

        searchResultsPage.shouldLoadRequired().breadcrumb.activeBreadcrumbItem.shouldHave(exactText("Results: $searchString"))
        searchResultsPage.breadcrumb.breadcrumbItems[0].shouldHave(exactText("Home"))
        Assertions.assertEquals(maxResultsPerPageExpected, searchResultsPage.searchResults.shouldLoadRequired().count())
        searchResultsPage.pagination.shouldLoadRequired().currentPage.shouldHave(exactText("1"))
        searchResultsPage.pagination.nextPage.shouldBe(visible)
        searchResultsPage.pagination.pagesLinks.shouldHave(size(resultsPagesExpected))[resultsPagesExpected - 2].shouldHave(
            exactText(resultsPagesExpected.toString())
        )
        searchResultsPage.pagination.pagesLinks.find(exactText(resultsPagesExpected.toString())).click()

        searchResultsPage.shouldLoadRequired().pagination.shouldLoadRequired().currentPage.shouldHave(
            exactText(
                resultsPagesExpected.toString()
            )
        )
        searchResultsPage.pagination.nextPage.should(disappear)
        searchResultsPage.pagination.previousPage.shouldBe(visible)
        Assertions.assertEquals(lastPageResultsExpected, searchResultsPage.searchResults.shouldLoadRequired().count())
        val result = searchResultsPage.searchResults.filterBy(text(lastPageResultTitle)).shouldHave(size(1))[0]
        result.title.shouldHave(exactText(lastPageResultTitle))
        result.text.shouldHave(text(lastPageResultText))
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun searchRequiredError(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.shouldLoadRequired().mainBanner.verifyTextsEs()
        homePage.mainMenu.searchOpen.click()
        homePage.mainMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys("Mexico")
        homePage.mainMenu.searchMenu.doSearch.click()
        homePage.mainMenu.searchMenu.should(disappear)

        val error =
            Assertions.assertThrows(RequiredError::class.java) { searchResultsErrorPage.shouldLoadRequired().searchResultsError.shouldLoadRequired() }
        Assertions.assertEquals(5, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    fun searchCollectionError(browserConfig: String) {
        setUpBrowser(browserConfig)
        homePage.shouldLoadRequired().acceptCookies()
        homePage.shouldLoadRequired().mainBanner.verifyTextsEs()
        homePage.mainMenu.searchOpen.click()
        homePage.mainMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys("Mexico")
        homePage.mainMenu.searchMenu.doSearch.click()
        homePage.mainMenu.searchMenu.should(disappear)

        val error =
            Assertions.assertThrows(RequiredError::class.java) { searchResultsCollectionErrorPage.shouldLoadRequired().searchResultsError.shouldLoadRequired() }
        Assertions.assertEquals(1, error.suppressed.size)
    }
}
