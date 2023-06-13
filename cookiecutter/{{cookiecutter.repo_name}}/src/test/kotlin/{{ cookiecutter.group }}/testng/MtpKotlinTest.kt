package {{ cookiecutter.group }}.testng

import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import org.apache.logging.log4j.kotlin.Logging
import org.testng.Assert
import org.testng.ITestResult
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Parameters
import org.testng.annotations.Test
import {{ cookiecutter.group }}.pom.pages.home.homePage
import {{ cookiecutter.group }}.pom.pages.services.qualityAssurancePage
import {{ cookiecutter.group }}.pom.pages.services.servicesPage
import {{ cookiecutter.group }}.pom.searchresults.searchResultsPage
import {{ cookiecutter.group }}.util.ReportHelper

open class MtpKotlinTest : Logging {

    private val maxResultsPerPageExpected = 5

    @BeforeMethod(description = "Open base URL in browser", alwaysRun = true)
    @Parameters("browser", "mobile", "env", "lang")
    fun beforeMethod(browser: String, mobile: String, env: String, lang: String) {
        SPConfig.resetConfig()
        // Configure webdriver
        if (mobile.equals("true", true)) {
            SPConfig.setupBasicMobileBrowser()
        } else {
            SPConfig.setupBasicDesktopBrowser(browser)
        }
        SPConfig.setCurrentThreadDriver()

        // Set env
        TestData.init(env)

        // Open URL and set up site
        homePage.open()
        SPConfig.lang = lang
        homePage.setLangIfNeeded()
        homePage.acceptCookies()
    }

    @AfterMethod(description = "Close browser", alwaysRun = true)
    fun afterMethod(result: ITestResult) {
        // Attach screenshot
        if (result.status != ITestResult.SUCCESS) {
            ReportHelper.attachScreenshot("Test failed screenshot")
        }

        // Quit webdriver
        SPConfig.quitCurrentThreadDriver()
        logger.info { "Closed webdriver for test ${result.name}. Status: ${result.status}" }
    }

    @Test(
        description = "User navigate to Quality Assurance (desktop)",
        groups = ["desktop"]
    )
    fun userNavigateToQualityAssuranceDesktop() {
        homePage.desktopMenu.services.hover()
        homePage.desktopMenu.servicesPopUp.qualityAssurance.click()
        qualityAssurancePage.shouldLoadRequired()
    }

    @Test(
        description = "User navigate to Quality Assurance (mobile)",
        groups = ["mobile"]
    )
    fun userNavigateToQualityAssuranceMobile() {
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        qualityAssurancePage.shouldLoadRequired()
    }

    @Test(
        description = "Forced failure",
        groups = ["desktop", "mobile"]
    )
    fun forcedFailure() {
        servicesPage.shouldLoadRequired()
    }

    @Test(
        description = "Cookies should not reappear after accepted (desktop)",
        groups = ["desktop"]
    )
    fun userNavigateToQualityAssuranceCookiesShouldNotReappearDesktop() {
        homePage.desktopMenu.services.hover()
        homePage.desktopMenu.servicesPopUp.qualityAssurance.click()
        qualityAssurancePage.shouldLoadRequired().cookiesBanner.shouldNotBe(visible)
    }

    @Test(
        description = "Cookies should not reappear after accepted (mobile)",
        groups = ["mobile"]
    )
    fun userNavigateToQualityAssuranceCookiesShouldNotReappearMobile() {
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        qualityAssurancePage.shouldLoadRequired().cookiesBanner.shouldNotBe(visible)
    }

    @Test(description = "Search (desktop)", groups = ["desktop.search"])
    @Parameters(
        "search",
        "resultsPagesExpected",
        "lastPageResultsExpected",
        "lastPageResultTitle",
        "lastPageResultText"
    )
    fun search(
        search: String,
        resultsPagesExpected: String,
        lastPageResultsExpected: String,
        lastPageResultTitle: String,
        lastPageResultText: String
    ) {
        homePage.desktopMenu.searchOpen.click()
        homePage.desktopMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys(search)
        homePage.desktopMenu.searchMenu.doSearch.click()
        homePage.desktopMenu.searchMenu.should(disappear)

        searchResultsPage.shouldLoadRequired().breadcrumb.activeBreadcrumbItem.shouldHave(exactText("Results: $search"))
        searchResultsPage.breadcrumb.breadcrumbItems[0].shouldHave(exactText("Home"))
        Assert.assertEquals(searchResultsPage.searchResults.shouldLoadRequired().count(), maxResultsPerPageExpected)
        if (resultsPagesExpected.toInt() > 1) {
            searchResultsPage.pagination.shouldLoadRequired().currentPage.shouldHave(exactText("1"))
            searchResultsPage.pagination.nextPage.shouldBe(visible)
            searchResultsPage.pagination.pagesLinks.shouldHave(size(resultsPagesExpected.toInt()))[resultsPagesExpected.toInt() - 2].shouldHave(
                exactText(resultsPagesExpected)
            )
            searchResultsPage.pagination.pagesLinks.find(exactText(resultsPagesExpected)).click()

            searchResultsPage.shouldLoadRequired().pagination.shouldLoadRequired().currentPage.shouldHave(
                exactText(resultsPagesExpected)
            )
            searchResultsPage.pagination.nextPage.should(disappear)
            searchResultsPage.pagination.previousPage.shouldBe(visible)
        } else {
            searchResultsPage.pagination.shouldNotBe(visible)
        }
        Assert.assertEquals(
            searchResultsPage.searchResults.shouldLoadRequired().count(),
            lastPageResultsExpected.toInt()
        )
        val result = searchResultsPage.searchResults.filterBy(text(lastPageResultTitle)).shouldHave(size(1))[0]
        result.title.shouldHave(exactText(lastPageResultTitle))
        if (lastPageResultText.isNotEmpty()) {
            result.text.shouldHave(text(lastPageResultText))
        } else {
            result.text.shouldNotBe(visible)
        }
    }
}
