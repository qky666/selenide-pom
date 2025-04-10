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
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import {{ cookiecutter.group }}.common_web.testng.Retry
import {{ cookiecutter.group }}.common_web.util.AllureReportHelper
import {{ cookiecutter.group }}.tiddlywikitestng.pom.MainPage
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
        Page.getInstance(MainPage::class).let {
            it.shouldLoadRequired(lang = "spa").changeSiteLanguageIfNeeded()
            val firstTiddler = it.storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
            GettingStartedTiddlerViewWidget(firstTiddler).shouldLoadRequired()
            it.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(1))
        }
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
        Page.getInstance(MainPage::class).let {
            it.showHideSidebar()
            it.sidebar.sidebarTabs.openTabButton.click()
            it.sidebar.sidebarTabs.openTabContent.shouldLoadRequired().closeAll.click(ClickOptions.usingJavaScript())
            it.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
            it.storyRiver.tiddlerViews.shouldHave(size(0))
            it.storyRiver.tiddlerEdits.shouldHave(size(0))
        }
    }

    @Test(description = "Verificar pestañas del panel lateral", groups = ["desktop", "mobile"])
    fun verifySidebarTabs() {
        Page.getInstance(MainPage::class).let {
            it.showHideSidebar()
            it.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
            it.storyRiver.tiddlerEdits.shouldHave(size(1))
            it.storyRiver.tiddlerViews.shouldHave(size(1))
            it.sidebar.sidebarTabs.tabButtonToTabContentMap.forEach { (tabButton, tabContent) ->
                tabButton.click()
                tabContent.shouldLoadRequired()
            }
            it.sidebar.sidebarTabs.recentTabButton.click()
            it.sidebar.sidebarTabs.recentTabContent.shouldLoadRequired().dateItems.shouldHave(size(1))
        }
    }

    @Suppress("TestFailedLine", "RedundantSuppression")
    @Test(description = "Error forzado: Existe un tiddler abierto no esperado", groups = ["desktop", "mobile"])
    fun forcedError() {
        Page.getInstance(MainPage::class).shouldLoadRequired().storyRiver.tiddlerViews.shouldHave(size(0))
    }

    @Test(description = "Crear y buscar nuevo tiddler", groups = ["desktop.search", "mobile.search"])
    @Parameters("title", "body", "searchResult")
    fun createAndSearchNewTiddler(title: String, body: String, searchResult: String) {
        // New tiddler
        Page.getInstance(MainPage::class).let {
            it.showHideSidebar()
            it.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
            val edits = it.storyRiver.tiddlerEdits
            val views = it.storyRiver.tiddlerViews
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
            it.sidebar.sidebarTabs.openTabButton.click()
            it.sidebar.sidebarTabs.openTabContent.shouldLoadRequired().closeAll.click(ClickOptions.usingJavaScript())
            it.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
            views.shouldHave(size(0))
            edits.shouldHave(size(0))

            // Search
            it.sidebar.searchInput.value = title
            it.sidebar.searchResultsText.shouldHave(text(searchResult))
            it.searchPopup.shouldLoadRequired().matches.shouldHave(size(2))[0].click()
            views.shouldHave(size(1))
            edits.shouldHave(size(0))

            // Verify tiddler data
            val foundTiddlerView = it.shouldLoadRequired().storyRiver.tiddlerViews.shouldHave(sizeGreaterThan(0))[0]
            foundTiddlerView.title.shouldHave(exactText(title))
            foundTiddlerView.body.shouldHave(exactText(body))

            // Close search popup
            it.sidebar.resetSearch.click()
            it.searchPopup.should(disappear)
        }
    }

    @Test(description = "WIP. Verificar botón 'Abiertos: Cerrar todo'", groups = ["wip", "desktop"])
    fun verifyCloseAllButtonWip() {
        Page.getInstance(MainPage::class).let {
            it.showHideSidebar()
            it.sidebar.sidebarTabs.openTabButton.click()
            it.sidebar.sidebarTabs.openTabContent.shouldLoadRequired().closeAll.click(ClickOptions.usingJavaScript())
            it.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
            it.storyRiver.tiddlerViews.shouldHave(size(0))
            it.storyRiver.tiddlerEdits.shouldHave(size(0))
        }
    }
}
