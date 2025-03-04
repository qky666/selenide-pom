package {{ cookiecutter.group }}.tiddlywikitestng.testng

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.CollectionCondition.sizeGreaterThan
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import {{ cookiecutter.group }}.common_web.testng.Retry
import {{ cookiecutter.group }}.common_web.util.AllureReportHelper
import {{ cookiecutter.group }}.tiddlywikitestng.pom.mainPage
import {{ cookiecutter.group }}.tiddlywikitestng.pom.storyriver.GettingStartedTiddlerViewWidget
import org.apache.logging.log4j.kotlin.Logging
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxOptions
import org.testng.ITestResult
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Parameters
import org.testng.annotations.Test

@Retry
class TestngTest : Logging {

    @BeforeMethod(description = "Open base URL in browser", alwaysRun = true)
    @Parameters("browser", "mobile", "env", "lang")
    fun beforeMethod(browser: String, mobile: String, env: String, lang: String) {
        SPConfig.resetConfig()

        // Set env
        TestData.init(env)

        // Configure webdriver
        if (mobile.equals("true", true)) SPConfig.setupBasicMobileBrowser()
        else SPConfig.setupBasicDesktopBrowser(browser)
        val config = SPConfig.selenideConfig
        config.remote()?.let {
            val options = if (browser.equals("firefox", ignoreCase = true)) {
                FirefoxOptions()
            } else if (browser.equals("chrome", ignoreCase = true)) {
                ChromeOptions()
            } else {
                throw RuntimeException("Invalid browser: $browser")
            }
            options.setPlatformName("linux")
            val merge = config.browserCapabilities().merge(options)
            config.browserCapabilities(merge)
        }

        SPConfig.setDriver()

        // Open URL
        SPConfig.lang = lang
        Selenide.open(TestData.getString("project.baseUrl")!!)

        // Set up site
        mainPage.shouldLoadRequired(lang = "spa").changeSiteLanguageIfNeeded()
        val firstTiddler = mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
        GettingStartedTiddlerViewWidget(firstTiddler).shouldLoadRequired()
        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(1))
    }

    @AfterMethod(description = "Close browser", alwaysRun = true)
    fun afterMethod(result: ITestResult) {
        // Attach screenshot
        if (result.status != ITestResult.SUCCESS) AllureReportHelper.attachScreenshot("Test failed screenshot")

        // Quit webdriver
        SPConfig.quitDriver()
        logger.info { "Closed webdriver for test ${result.name}. Status: ${result.status}" }
    }

    @Test(description = "Verificar botón 'Abiertos: Cerrar todo'", groups = ["desktop", "mobile"])
    fun verifyCloseAllButton() {
        mainPage.showHideSidebar()
        mainPage.sidebar.sidebarTabs.openTabButton.click()
        mainPage.sidebar.sidebarTabs.openTabContent.shouldLoadRequired().closeAll.click(ClickOptions.usingJavaScript())
        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
        mainPage.storyRiver.tiddlerViews.shouldHave(size(0))
        mainPage.storyRiver.tiddlerEdits.shouldHave(size(0))
    }

    @Test(description = "Verificar pestañas del panel lateral", groups = ["desktop", "mobile"])
    fun verifySidebarTabs() {
        mainPage.showHideSidebar()
        mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
        mainPage.storyRiver.tiddlerEdits.shouldHave(size(1))
        mainPage.storyRiver.tiddlerViews.shouldHave(size(1))

        mainPage.sidebar.sidebarTabs.tabButtonToTabContentMap.forEach { (tabButton, tabContent) ->
            tabButton.click()
            tabContent.shouldLoadRequired()
        }

        mainPage.sidebar.sidebarTabs.recentTabButton.click()
        mainPage.sidebar.sidebarTabs.recentTabContent.shouldLoadRequired().dateItems.shouldHave(size(1))
    }

    @Suppress("TestFailedLine", "RedundantSuppression")
    @Test(description = "Error forzado: Existe un tiddler abierto no esperado", groups = ["desktop", "mobile"])
    fun forcedError() {
        mainPage.shouldLoadRequired().storyRiver.tiddlerViews.shouldHave(size(0))
    }

    @Test(description = "Crear y buscar nuevo tiddler", groups = ["desktop.search", "mobile.search"])
    @Parameters("title", "body", "searchResult")
    fun createAndSearchNewTiddler(title: String, body: String, searchResult: String) {
        // New tiddler
        mainPage.showHideSidebar()
        mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
        val edits = mainPage.storyRiver.tiddlerEdits
        val views = mainPage.storyRiver.tiddlerViews
        edits.shouldHave(size(1))
        views.shouldHave(size(1))

        // New tiddler data
        val edit = edits[0].shouldLoadRequired()
        edit.titleInput.value = title
        // Selenide helpers for shadow dom not working here (do not know why), so we do it the hard way with switchTo
        val webdriver = SPConfig.getWebDriver()!!
        webdriver.switchTo().frame(edit.bodyEditorIframe.wrappedElement)
        webdriver.findElement(By.cssSelector("textarea")).sendKeys(body)
        webdriver.switchTo().defaultContent()
        edit.save.click()
        edits.shouldHave(size(0))
        val newTiddlerView = views.shouldHave(size(2))[0]
        newTiddlerView.title.shouldHave(exactText(title))
        newTiddlerView.body.shouldHave(exactText(body))

        // Close all
        mainPage.sidebar.sidebarTabs.openTabButton.click()
        mainPage.sidebar.sidebarTabs.openTabContent.shouldLoadRequired().closeAll.click(ClickOptions.usingJavaScript())
        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
        views.shouldHave(size(0))
        edits.shouldHave(size(0))

        // Search
        mainPage.sidebar.searchInput.value = title
        mainPage.sidebar.searchResultsText.shouldHave(text(searchResult))
        mainPage.searchPopup.shouldLoadRequired().matches.shouldHave(size(2))[0].click()
        views.shouldHave(size(1))
        edits.shouldHave(size(0))

        // Verify tiddler data
        val foundTiddlerView = mainPage.shouldLoadRequired().storyRiver.tiddlerViews.shouldHave(sizeGreaterThan(0))[0]
        foundTiddlerView.title.shouldHave(exactText(title))
        foundTiddlerView.body.shouldHave(exactText(body))

        // Close search popup
        mainPage.sidebar.resetSearch.click()
        mainPage.searchPopup.should(disappear)
    }

    @Test(description = "WIP. Verificar botón 'Abiertos: Cerrar todo'", groups = ["wip", "desktop"])
    fun verifyCloseAllButtonWip() {
        mainPage.showHideSidebar()
        mainPage.sidebar.sidebarTabs.openTabButton.click()
        mainPage.sidebar.sidebarTabs.openTabContent.shouldLoadRequired().closeAll.click(ClickOptions.usingJavaScript())
        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
        mainPage.storyRiver.tiddlerViews.shouldHave(size(0))
        mainPage.storyRiver.tiddlerEdits.shouldHave(size(0))
    }
}
