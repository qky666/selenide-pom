package {{ cookiecutter.group }}.mtptestng.testng

import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import {{ cookiecutter.group }}.common.testng.Retry
import {{ cookiecutter.group }}.common.util.AllureReportHelper
import {{ cookiecutter.group }}.mtptestng.pom.pages.home.homePage
import {{ cookiecutter.group }}.mtptestng.pom.pages.searchresults.searchResultsPage
import {{ cookiecutter.group }}.mtptestng.pom.pages.services.qualityAssurancePage
import {{ cookiecutter.group }}.mtptestng.pom.pages.services.servicesPage
import org.apache.logging.log4j.kotlin.Logging
import org.testng.Assert
import org.testng.ITestResult
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Parameters
import org.testng.annotations.Test

@Retry
class TestngTest : Logging {

    private val maxResultsPerPageExpected = 5

    @BeforeMethod(description = "Open base URL in browser", alwaysRun = true)
    @Parameters("browser", "mobile", "env", "lang")
    fun beforeMethod(browser: String, mobile: String, env: String, lang: String) {
        SPConfig.resetConfig()
        // Configure webdriver
        if (mobile.equals("true", true)) SPConfig.setupBasicMobileBrowser()
        else SPConfig.setupBasicDesktopBrowser(browser)
        SPConfig.setDriver()

        // Set env
        TestData.init(env)

        // Open URL and set up site
        homePage.open()
        homePage.acceptCookies("es")
        SPConfig.lang = lang
        homePage.setLangIfNeeded()
    }

    @AfterMethod(description = "Close browser", alwaysRun = true)
    fun afterMethod(result: ITestResult) {
        // Attach screenshot
        if (result.status != ITestResult.SUCCESS) AllureReportHelper.attachScreenshot("Test failed screenshot")

        // Quit webdriver
        SPConfig.quitDriver()
        logger.info { "Closed webdriver for test ${result.name}. Status: ${result.status}" }
    }

    @Test(description = "User navigate to Quality Assurance (desktop)", groups = arrayOf("desktop"))
    fun userNavigateToQualityAssuranceDesktop() {
        homePage.desktopMenu.services.hover()
        homePage.desktopMenu.servicesPopUp.qualityAssurance.click()
        qualityAssurancePage.shouldLoadRequired()
    }

    @Test(description = "User navigate to Quality Assurance (mobile)", groups = arrayOf("mobile"))
    fun userNavigateToQualityAssuranceMobile() {
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        qualityAssurancePage.shouldLoadRequired()
    }

    @Test(description = "Forced failure", groups = arrayOf("desktop", "mobile"))
    fun forcedFailure() {
        servicesPage.shouldLoadRequired()
    }

    @Test(description = "Cookies should not reappear after accepted (desktop)", groups = arrayOf("desktop"))
    fun userNavigateToQualityAssuranceCookiesShouldNotReappearDesktop() {
        homePage.desktopMenu.services.hover()
        homePage.desktopMenu.servicesPopUp.qualityAssurance.click()
        qualityAssurancePage.shouldLoadRequired().cookiesBanner.shouldNotBe(visible)
    }

    @Test(description = "Cookies should not reappear after accepted (mobile)", groups = arrayOf("mobile"))
    fun userNavigateToQualityAssuranceCookiesShouldNotReappearMobile() {
        homePage.mobileMenu.mobileMenuButton.click()
        val mobileMenu = homePage.mobileMenuPopUp
        mobileMenu.shouldLoadRequired().shouldBeCollapsed()
        mobileMenu.services().click()
        mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
        qualityAssurancePage.shouldLoadRequired().cookiesBanner.shouldNotBe(visible)
    }

    @Test(description = "Search (desktop)", groups = arrayOf("desktop.search"))
    @Parameters(
        "search", "resultsPagesExpected", "lastPageResultsExpected", "lastPageResultTitle", "lastPageResultText"
    )
    fun search(
        search: String,
        resultsPagesExpected: String,
        lastPageResultsExpected: String,
        lastPageResultTitle: String,
        lastPageResultText: String
    ) {
        val menu = homePage.desktopMenu
        menu.searchOpen.click()
        menu.searchMenu.shouldLoadRequired().searchInput.sendKeys(search)
        menu.searchMenu.doSearch.click()
        menu.searchMenu.should(disappear)

        searchResultsPage.shouldLoadRequired().breadcrumb.activeBreadcrumbItem.shouldHave(exactText("Results: $search"))
        searchResultsPage.breadcrumb.breadcrumbItems[0].shouldHave(exactText("Home"))
        val pages = resultsPagesExpected.toInt()
        val pagination = searchResultsPage.pagination
        if (pages > 1) {
            Assert.assertEquals(searchResultsPage.searchResults.shouldLoadRequired().size(), maxResultsPerPageExpected)
            pagination.shouldLoadRequired().currentPage.shouldHave(exactText("1"))
            pagination.nextPage.shouldBe(visible)
            pagination.pagesLinks.shouldHave(size(pages))[pages - 2].shouldHave(exactText(resultsPagesExpected))
            pagination.pagesLinks.find(exactText(resultsPagesExpected)).click()

            searchResultsPage.shouldLoadRequired()
            pagination.shouldLoadRequired().currentPage.shouldHave(exactText(resultsPagesExpected))
            pagination.nextPage.should(disappear)
            pagination.previousPage.shouldBe(visible)
        } else pagination.shouldNotBe(visible)
        val results = lastPageResultsExpected.toInt()
        Assert.assertEquals(searchResultsPage.searchResults.shouldLoadRequired().size(), results)
        val result = searchResultsPage.searchResults.filterBy(text(lastPageResultTitle)).shouldHave(size(1))[0]
        result.title.shouldHave(exactText(lastPageResultTitle))
        if (lastPageResultText.isNotEmpty()) result.text.shouldHave(text(lastPageResultText))
        else result.text.shouldNotBe(visible)
    }

    @Test(description = "WIP. User navigate to Quality Assurance (desktop)", groups = arrayOf("desktop", "wip"))
    fun userNavigateToQualityAssuranceDesktopWip() {
        homePage.desktopMenu.services.hover()
        homePage.desktopMenu.servicesPopUp.qualityAssurance.click()
        qualityAssurancePage.shouldLoadRequired()
    }
}
