package com.github.qky666.selenidepom.test.kotlin.tiddlywiki

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.ex.ElementNotFound
import com.codeborne.selenide.ex.UIAssertionError
import com.github.qky666.selenidepom.condition.langCondition
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.ConditionNotDefinedError
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.mainPage
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.storyriver.GettingStartedTiddlerViewWidget
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.openqa.selenium.By
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TiddlywikiTest {

    companion object {
        @JvmStatic
        fun browserConfigAndLangSource(): List<Arguments> {
            return listOf(
                Arguments.of("chrome", "es"),
                Arguments.of("chrome", "en"),
                Arguments.of("firefox", "es"),
                Arguments.of("firefox", "en"),
                Arguments.of("chromeMobile", "es"),
                Arguments.of("chromeMobile", "en")
            )
        }
    }

    @BeforeEach
    fun beforeEach() {
        SPConfig.resetConfig()

        // Additional test for output in TestData
        TestData.init("tiddlywiki-prod")
        TestData.output["threadId"] = Thread.currentThread().id
    }

    @AfterEach
    fun afterEach() {
        SPConfig.quitCurrentThreadDriver()
        // Additional test for output in TestData
        Assertions.assertEquals(TestData.output["threadId"].toString(), Thread.currentThread().id.toString())
    }

    private fun setupSite(browserConfig: String, lang: String = "en") {
        if (browserConfig.equals("chromeMobile", ignoreCase = true)) {
            SPConfig.setupBasicMobileBrowser()
            SPConfig.model = "mobile"
        } else {
            SPConfig.setupBasicDesktopBrowser(browserConfig)
            SPConfig.model = "desktop"
        }
        SPConfig.setCurrentThreadDriver()
        SPConfig.lang = lang
        val relativeUrl = TestData.input.getProperty("data.input.relativeUrl")
        val url = Thread.currentThread().contextClassLoader.getResource(relativeUrl)
        assertNotNull(url, "URL not found")
        Selenide.open(url)
        changeSiteLanguageIfNeeded()
    }

    private fun changeSiteLanguageIfNeeded(newLang: String = SPConfig.lang, currentLang: String = "es") {
        if (!newLang.contentEquals(currentLang, true)) {
            if (mainPage.shouldLoadRequired(lang = currentLang).showSidebar.isDisplayed) {
                mainPage.showSidebar.click()
                mainPage.sidebar.shouldLoadRequired(lang = currentLang)
            }
            mainPage.sidebar.sidebarTabs.toolsTabButton.click()
            mainPage.sidebar.sidebarTabs.toolsTabContent.shouldLoadRequired(lang = currentLang).language.button.click()
            mainPage.sidebar.sidebarTabs.toolsTabContent.languageChooser.shouldLoadRequired(lang = currentLang)
            if (newLang.contentEquals("es", true)) {
                mainPage.sidebar.sidebarTabs.toolsTabContent.languageChooser.esES.click()
            } else {
                mainPage.sidebar.sidebarTabs.toolsTabContent.languageChooser.enGB.click()
            }
            mainPage.sidebar.sidebarTabs.toolsTabContent.languageChooser.should(disappear)
            mainPage.shouldLoadRequired(lang = newLang)
            mainPage.sidebar.sidebarTabs.openTabButton.click()
            mainPage.shouldLoadRequired(lang = newLang)
        }
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun verifyGettingStartedAndCloseAllTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)
        val firstTiddler = mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
        // GettingStartedTiddlerViewWidget(firstTiddler).shouldLoadRequired()
        GettingStartedTiddlerViewWidget(Page.Companion.findAll(listOf(firstTiddler.toWebElement()))[0]).shouldLoadRequired()

        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(1))
        mainPage.sidebar.sidebarTabs.openTabContent.closeAll.click()
        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
        mainPage.storyRiver.tiddlerViews.shouldHave(size(0))
        mainPage.storyRiver.tiddlerEdits.shouldHave(size(0))
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun verifySidebarTabsTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)

        // Populate Recent tab.
        // newTiddler works better with javascript click
        mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
        mainPage.storyRiver.tiddlerEdits.shouldHave(size(1))

        for ((tabButton, tabContent) in mainPage.sidebar.sidebarTabs.tabButtonToTabContentMap) {
            tabButton.click()
            tabContent.shouldLoadRequired()
        }

        mainPage.sidebar.sidebarTabs.recentTabButton.click()
        mainPage.sidebar.sidebarTabs.recentTabContent.shouldLoadRequired().dateItems.shouldHave(size(1))
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun showHideSidebarTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)

        mainPage.sidebar.shouldLoadRequired()
        mainPage.hideShowSidebar.click()
        mainPage.sidebar.shouldNotBe(visible)
        mainPage.showSidebar.click()
        mainPage.sidebar.shouldLoadRequired()
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun createAndSearchNewTiddlerTest(browserConfig: String, lang: String) {
        val newTiddlerTitle = "My new tiddler title"
        val newTiddlerBody = "My new tiddler body"

        setupSite(browserConfig, lang)

        // newTiddler works better with javascript click
        mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
        val newTiddlerEdit = mainPage.storyRiver.tiddlerEdits.shouldHave(size(1))[0].shouldLoadRequired()
        newTiddlerEdit.titleInput.value = newTiddlerTitle
        // Selenide helpers for shadow dom not working here (do not know the reason), so we do it the hard way with switchTo
        val webdriver = Selenide.webdriver().`object`()
        webdriver.switchTo().frame(newTiddlerEdit.bodyEditorIframe.wrappedElement)
        webdriver.findElement(By.cssSelector("textarea")).sendKeys(newTiddlerBody)
        webdriver.switchTo().defaultContent()
        newTiddlerEdit.save.click()
        val newTiddlerView = mainPage.storyRiver.tiddlerViews.shouldHave(size(2))[0]
        newTiddlerView.title.shouldHave(exactText(newTiddlerTitle))
        newTiddlerView.body.shouldHave(exactText(newTiddlerBody))

        // Close all
        mainPage.sidebar.sidebarTabs.openTabContent.closeAll.click(ClickOptions.usingJavaScript())
        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
        mainPage.storyRiver.tiddlerViews.shouldHave(size(0))
        mainPage.storyRiver.tiddlerEdits.shouldHave(size(0))

        // Search
        mainPage.sidebar.searchInput.value = newTiddlerTitle
        val firstSearchResult = mainPage.searchPopup.shouldLoadRequired().matches.shouldHave(size(2))[0]
        mainPage.sidebar.searchResultsText.shouldBe(
            langCondition(
                mapOf(
                    "en" to "1 matches", "es" to "1 coincidencias"
                )
            )
        )


        firstSearchResult.click()

        val tiddlerSearchResult = mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0]
        tiddlerSearchResult.title.shouldHave(exactText(newTiddlerTitle))
        tiddlerSearchResult.body.shouldHave(exactText(newTiddlerBody))
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun createAndSearchNewTiddlerWithIncompleteLangConditionTest(browserConfig: String, lang: String) {
        val newTiddlerTitle = "My new tiddler title"
        val newTiddlerBody = "My new tiddler body"

        setupSite(browserConfig, lang)

        // newTiddler works better with javascript click
        mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
        val newTiddlerEdit = mainPage.storyRiver.tiddlerEdits.shouldHave(size(1))[0].shouldLoadRequired()
        newTiddlerEdit.titleInput.value = newTiddlerTitle
        // Selenide helpers for shadow dom not working here (do not know the reason), so we do it the hard way with switchTo
        val webdriver = Selenide.webdriver().`object`()
        webdriver.switchTo().frame(newTiddlerEdit.bodyEditorIframe.wrappedElement)
        webdriver.findElement(By.cssSelector("textarea")).sendKeys(newTiddlerBody)
        webdriver.switchTo().defaultContent()
        newTiddlerEdit.save.click()
        val newTiddlerView = mainPage.storyRiver.tiddlerViews.shouldHave(size(2))[0]
        newTiddlerView.title.shouldHave(exactText(newTiddlerTitle))
        newTiddlerView.body.shouldHave(exactText(newTiddlerBody))

        // Close all
        mainPage.sidebar.sidebarTabs.openTabContent.closeAll.click(ClickOptions.usingJavaScript())
        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
        mainPage.storyRiver.tiddlerViews.shouldHave(size(0))
        mainPage.storyRiver.tiddlerEdits.shouldHave(size(0))

        // Search
        mainPage.sidebar.searchInput.value = newTiddlerTitle
        val firstSearchResult = mainPage.searchPopup.shouldLoadRequired().matches.shouldHave(size(2))[0]
        mainPage.sidebar.searchResultsText.shouldBe(
            langCondition(
                mapOf(
                    "en" to "1 matches", "es" to "1 coincidencias"
                )
            )
        )

        // Incomplete LangCondition (non strict)
        mainPage.sidebar.searchResultsText.shouldBe(langCondition(mapOf("en" to exactText("1 matches")), false))

        // Incomplete LangCondition
        // val incompleteLangCondition = langCondition(mapOf("en" to "1 matches"))
        val incompleteLangCondition = langCondition(mapOf("en" to exactText("1 matches")))
        if (SPConfig.lang == "es") {
            val error = assertThrows<UIAssertionError> {
                mainPage.sidebar.searchResultsText.shouldBe(incompleteLangCondition)
            }
            assertNotNull(error.cause)
            assertEquals(error.cause!!::class, ConditionNotDefinedError::class)
        } else {
            mainPage.sidebar.searchResultsText.shouldBe(incompleteLangCondition)
        }

        firstSearchResult.click()

        val tiddlerSearchResult = mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0]
        tiddlerSearchResult.title.shouldHave(exactText(newTiddlerTitle))
        tiddlerSearchResult.body.shouldHave(exactText(newTiddlerBody))
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun shouldMeetConditionTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)
        val firstTiddler = mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
        val gettingStartedTiddler = GettingStartedTiddlerViewWidget(firstTiddler).shouldLoadRequired()
        gettingStartedTiddler.title.shouldMeetCondition()
        Assertions.assertTrue(gettingStartedTiddler.title.meetsCondition())

        val nonExistingElement = LangConditionedElement(
            Page.find("non-existing"), mapOf("en" to exactText("Non existing")), false
        )
        Assertions.assertEquals(SPConfig.lang == "es", nonExistingElement.meetsCondition())
        if (SPConfig.lang == "es") {
            nonExistingElement.shouldMeetCondition(Duration.ZERO)
        } else {
            assertThrows<ElementNotFound> {
                nonExistingElement.shouldMeetCondition(Duration.ZERO)
            }
        }

        val nonExistingStrictElement = LangConditionedElement(Page.find("non-existing"), mapOf("en" to "Non existing"))
        Assertions.assertEquals(false, nonExistingStrictElement.meetsCondition())
        if (SPConfig.lang == "es") {
            assertThrows<ConditionNotDefinedError> {
                nonExistingStrictElement.shouldMeetCondition(Duration.ZERO)
            }
        } else {
            assertThrows<ElementNotFound> {
                nonExistingStrictElement.shouldMeetCondition(Duration.ZERO)
            }
        }
    }
}
