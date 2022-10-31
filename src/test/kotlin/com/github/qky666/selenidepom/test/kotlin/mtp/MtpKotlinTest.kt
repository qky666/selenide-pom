package com.github.qky666.selenidepom.test.kotlin.mtp

import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide.closeWebDriver
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.open
import com.codeborne.selenide.ex.ElementNotFound
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.RequiredError
import com.github.qky666.selenidepom.pom.hasLoadedRequired
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.home.homePage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services.qualityAssurancePage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services.servicesRequiredErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services.servicesShouldLoadRequiredErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults.searchResultsCollectionErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults.searchResultsErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults.searchResultsPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration

class MtpKotlinTest {

    private val testData = TestData("prod")

    private val maxResultsPerPageExpected = 5

    companion object {
        @JvmStatic
        fun desktopBrowserConfigAndLangSource(): List<Arguments> {
            return listOf(
                Arguments.of("chrome", "es"),
                Arguments.of("chrome", "en"),
                Arguments.of("firefox", "es"),
                Arguments.of("chrome", "en"),
            )
        }

        @JvmStatic
        fun mobileBrowserConfigAndLangSource(): List<Arguments> {
            return listOf(
                Arguments.of("chromeMobile", "es"),
                Arguments.of("chromeMobile", "en"),
            )
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
    @MethodSource("desktopBrowserConfigAndLangSource")
    fun userNavigateToQualityAssuranceDesktop(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().desktopMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.desktopMenu.services.hover()
        homePage.desktopMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        qualityAssurancePage.shouldLoadRequired()
        Assertions.assertEquals(
            "div#cookie-law-info-bar/a#cookie_action_close_header",
            qualityAssurancePage.cookiesBanner.accept.searchCriteria
        )
        // Cookies message should not reappear
        qualityAssurancePage.cookiesBanner.shouldNotBe(visible)
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigAndLangSource")
    fun badSelectorErrorDesktop(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().desktopMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.desktopMenu.services.hover()
        homePage.desktopMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        qualityAssurancePage.shouldLoadRequired()
        val badSelector = element("bad-selector")
        Assertions.assertThrows(ElementNotFound::class.java) { badSelector.click() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigAndLangSource")
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorDesktop(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().desktopMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.desktopMenu.services.hover()
        homePage.desktopMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        Assertions.assertThrows(RequiredError::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigAndLangSource")
    fun userNavigateToQualityAssuranceWithBadSelectorRequiredDesktop(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().desktopMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.desktopMenu.services.hover()
        homePage.desktopMenu.servicesPopUp.shouldLoadRequired().qualityAssurance.click()
        val error =
            Assertions.assertThrows(RequiredError::class.java) { servicesRequiredErrorPage.shouldLoadRequired() }
        Assertions.assertEquals(2, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigAndLangSource")
    fun userForgotClickDesktop(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().desktopMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.desktopMenu.services.hover()
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
    @MethodSource("mobileBrowserConfigAndLangSource")
    fun userNavigateToQualityAssuranceMobile(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().mobileMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
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
    @MethodSource("mobileBrowserConfigAndLangSource")
    fun badSelectorErrorMobile(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().mobileMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        qualityAssurancePage.shouldLoadRequired()
        val badSelector = element("bad-selector")
        Assertions.assertThrows(ElementNotFound::class.java) { badSelector.click() }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigAndLangSource")
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorMobile(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().mobileMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        Assertions.assertThrows(RequiredError::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigAndLangSource")
    fun userNavigateToQualityAssuranceWithBadSelectorRequiredMobile(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().mobileMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        val error =
            Assertions.assertThrows(RequiredError::class.java) { servicesRequiredErrorPage.shouldLoadRequired() }
        Assertions.assertEquals(2, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigAndLangSource")
    fun userForgotClickMobile(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().mobileMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
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
    @MethodSource("mobileBrowserConfigAndLangSource")
    fun userNavigateToQualityAssuranceDesktopWrongModel(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().mobileMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        Assertions.assertThrows(RequiredError::class.java) { qualityAssurancePage.shouldLoadRequired(model = "desktop") }
        Assertions.assertFalse(qualityAssurancePage.hasLoadedRequired(model = "desktop"))
        Assertions.assertFalse(
            qualityAssurancePage.hasLoadedRequired(
                timeout = Duration.ofMillis(100), lang = "desktop"
            )
        )
    }

    @ParameterizedTest
    @CsvSource(
        "chrome,es,Mexico,2,3,'MTP, 25 años como empresa de referencia en aseguramiento de negocios digitales','MTP es hoy una empresa de referencia en Digital Business Assurance'",
        "firefox,es,Mexico,2,3,'MTP, 25 años como empresa de referencia en aseguramiento de negocios digitales','MTP es hoy una empresa de referencia en Digital Business Assurance'",
        "chrome,es,Viajero,2,2,'Los valores MTP, claves para este 2020','Este año 2020 ha sido un año particular y totalmente atípico para todos'",
        "firefox,es,Viajero,2,2,'Los valores MTP, claves para este 2020','Este año 2020 ha sido un año particular y totalmente atípico para todos'",
        "chrome,en,Mexico,1,5,'Contact us',''",
        "firefox,en,Mexico,1,5,'Contact us',''",
    )
    fun search(
        browserConfig: String,
        lang: String,
        searchString: String,
        resultsPagesExpected: Int,
        lastPageResultsExpected: Int,
        lastPageResultTitle: String,
        lastPageResultText: String
    ) {

        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().desktopMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.desktopMenu.searchOpen.click()
        homePage.desktopMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys(searchString)
        homePage.desktopMenu.searchMenu.doSearch.click()
        homePage.desktopMenu.searchMenu.should(disappear)

        searchResultsPage.shouldLoadRequired().breadcrumb.activeBreadcrumbItem.shouldHave(exactText("Results: $searchString"))
        searchResultsPage.breadcrumb.breadcrumbItems[0].shouldHave(exactText("Home"))
        Assertions.assertEquals(maxResultsPerPageExpected, searchResultsPage.searchResults.shouldLoadRequired().count())
        if (resultsPagesExpected > 1) {
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
        } else {
            searchResultsPage.pagination.shouldNotBe(visible)
        }
        Assertions.assertEquals(lastPageResultsExpected, searchResultsPage.searchResults.shouldLoadRequired().count())
        val result = searchResultsPage.searchResults.filterBy(text(lastPageResultTitle)).shouldHave(size(1))[0]
        result.title.shouldHave(exactText(lastPageResultTitle))
        if (lastPageResultText.isNotEmpty()) {
            result.text.shouldHave(text(lastPageResultText))
        } else {
            result.text.shouldNotBe(visible)
        }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigAndLangSource")
    fun searchRequiredError(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().desktopMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.desktopMenu.searchOpen.click()
        homePage.desktopMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys("Mexico")
        homePage.desktopMenu.searchMenu.doSearch.click()
        homePage.desktopMenu.searchMenu.should(disappear)

        val error =
            Assertions.assertThrows(RequiredError::class.java) { searchResultsErrorPage.shouldLoadRequired().searchResultsError.shouldLoadRequired() }
        Assertions.assertEquals(5, error.suppressed.size)
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigAndLangSource")
    fun searchCollectionError(browserConfig: String, lang: String) {
        setUpBrowser(browserConfig)
        if (lang.contentEquals("en", ignoreCase = true)) {
            homePage.shouldLoadRequired().desktopMenu.langEn.click()
            SPConfig.lang = "en"
        }
        homePage.shouldLoadRequired().acceptCookies()
        homePage.desktopMenu.searchOpen.click()
        homePage.desktopMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys("Mexico")
        homePage.desktopMenu.searchMenu.doSearch.click()
        homePage.desktopMenu.searchMenu.should(disappear)

        val error =
            Assertions.assertThrows(RequiredError::class.java) { searchResultsCollectionErrorPage.shouldLoadRequired().searchResultsError.shouldLoadRequired() }
        Assertions.assertEquals(1, error.suppressed.size)
    }
}
