package com.github.qky666.selenidepom.test.kotlin.tiddlywiki

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.ElementsContainer
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.ex.ElementNotFound
import com.codeborne.selenide.ex.UIAssertionError
import com.github.qky666.selenidepom.condition.langCondition
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.ConditionNotDefinedError
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.RequiredError
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection
import com.github.qky666.selenidepom.pom.hasLoadedRequired
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.MainPage
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.mainPage
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.storyriver.GettingStartedTiddlerViewWidget
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

        @JvmStatic
        fun requiredSource(): List<Arguments> {
            return listOf(
                Arguments.of("chrome", "es", true, object : MainPage() {
                    @Required override val searchPopup = super.searchPopup
                }),
                Arguments.of("chrome", "en", true, object : MainPage() {
                    @Required val noExists = find(By.cssSelector("no-exists"))
                }),
                Arguments.of("firefox", "es", true, object : MainPage() {
                    @Required val noExistsCollection = findAll("no-exists")
                    @Required val doExistsCollection = findAll("body")
                }),
                Arguments.of("firefox", "en", true, object : MainPage() {
                    @Required val noExistsBy = By.cssSelector("no-exists")
                    @Required val doExistsBy = By.cssSelector("body")
                }),
                Arguments.of("chromeMobile", "es", true, object : MainPage() {
                    @Required val badElementsContainer = object : ElementsContainer() {
                        override fun getSelf(): SelenideElement {
                            return find("no-exists", 1)
                        }
                    }
                    @Required val goodElementsContainer = object : ElementsContainer() {
                        override fun getSelf(): SelenideElement {
                            return find(By.xpath(".//body"), 0)
                        }
                    }
                }),
                Arguments.of("chromeMobile", "en", true, object : MainPage() {
                    inner class MyWidget(self: SelenideElement) : Widget(self) {
                        @Required val noExists = Companion.find("no-exists")
                    }

                    @Required val widgetsCollection = WidgetsCollection(findAll("body"), ::MyWidget)
                    @Suppress("unused") val notRequiredWidgetsCollection =
                        WidgetsCollection(findAll("body"), ::MyWidget)
                }),
                Arguments.of("firefox", "es", true, object : MainPage() {
                    inner class MyWidget(self: SelenideElement) : Widget(self) {
                        @Required val doExists = Companion.find("body")
                    }

                    @Required val widgetsCollection = WidgetsCollection(findAll("no-exists"), ::MyWidget)
                }),
                Arguments.of("chrome", "es", true, object : MainPage() {
                    @Required
                    fun getParameterElement(
                        model: String = SPConfig.model, lang: String = SPConfig.lang
                    ): SelenideElement {
                        return find("no-exists-$model-$lang")
                    }
                }),
                Arguments.of("chrome", "en", true, object : MainPage() {
                    @Required
                    fun getElement(): SelenideElement {
                        return find("no-exists")
                    }
                }),
                Arguments.of("chrome", "es", false, object : MainPage() {
                    @Required(model = "mobile") val noExistsModel = find("no-exists")
                }),
                Arguments.of("firefox", "en", false, object : MainPage() {
                    @Required(model = "mobile") val noExistsModel = find("no-exists")
                }),
                Arguments.of("chromeMobile", "es", true, object : MainPage() {
                    @Required(model = "mobile") val noExistsModel = find("no-exists")
                }),
                Arguments.of("chrome", "es", true, object : MainPage() {
                    @Required(lang = "es") val noExistsLang = find("no-exists")
                }),
                Arguments.of("firefox", "en", false, object : MainPage() {
                    @Required(lang = "es") val noExistsLang = find("no-exists")
                }),
                Arguments.of("chromeMobile", "es", true, object : MainPage() {
                    @Required(lang = "es") val noExistsLang = find("no-exists")
                }),
                Arguments.of("firefox", "es", true, object : MainPage() {
                    @Required
                    fun getBadWebElement(): WebElement {
                        return this.sidebar.toWebElement()
                    }

                    @Required
                    fun getGoodWebElement(): WebElement {
                        return this.hideShowSidebar.toWebElement()
                    }

                    @Required
                    fun getOtherGoodWebElement(): WebElement {
                        return find(this.hideShowSidebar.toWebElement())
                    }
                }),
                Arguments.of("firefox", "en", true, object : MainPage() {
                    @Required val langConditionedElement = LangConditionedElement(find("no-exists"), "NoText")
                }),
                Arguments.of("chrome", "es", true, object : MainPage() {
                    @Required val langConditionedElement =
                        LangConditionedElement(super.hideShowSidebar, mapOf("es" to "BadText"))
                }),
                Arguments.of("chrome", "en", true, object : MainPage() {
                    @Required val langConditionedElement =
                        LangConditionedElement(super.hideShowSidebar, mapOf("es" to "BadText"))
                }),
                Arguments.of("firefox", "es", true, object : MainPage() {
                    @Required val langConditionedElement =
                        LangConditionedElement(super.hideShowSidebar, mapOf("es" to exactText("BadText")), false)
                }),
                Arguments.of("firefox", "en", false, object : MainPage() {
                    @Required val langConditionedElement =
                        LangConditionedElement(super.hideShowSidebar, mapOf("es" to exactText("BadText")), false)
                }),
            )
        }
    }

    @BeforeEach
    fun beforeEach() {
        SPConfig.resetConfig()
        TestData.init("tiddlywiki-prod")

        // Additional test for output in TestData
        TestData.output["threadId"] = Thread.currentThread().id
    }

    @AfterEach
    fun afterEach() {
        SPConfig.quitCurrentThreadDriver()
        // Additional test for output in TestData
        Assertions.assertEquals(TestData.output["threadId"].toString(), Thread.currentThread().id.toString())
    }

    private fun setupSite(browserConfig: String, lang: String = "es") {
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

        mainPage.sidebar.resetSearch.click()
        mainPage.searchPopup.should(disappear)
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

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun failedCustomShouldLoadRequiredTest(browserConfig: String, lang: String) {
        val myMainPage = object : MainPage() {
            override fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
                super.customShouldLoadRequired(timeout, model, lang)
                if (model.equals("mobile", true) or lang.equals("es", true)) {
                    find("no-exists").shouldBe(visible, Duration.ZERO)
                }
            }
        }

        setupSite(browserConfig, lang)

        if (browserConfig.equals("chromeMobile", true) or lang.equals("es", true)) {
            val requiredError = assertThrows<RequiredError> {
                myMainPage.shouldLoadRequired()
            }
            assertEquals(1, requiredError.suppressed.size)
        } else {
            myMainPage.shouldLoadRequired()
        }
    }

    @ParameterizedTest
    @MethodSource("requiredSource")
    fun requiredTest(browserConfig: String, lang: String, throwsError: Boolean, myMainPage: MainPage) {
        setupSite(browserConfig, lang)
        myMainPage.hideSidebar.click()
        myMainPage.showSidebar.shouldBe(visible)
        myMainPage.sidebar.should(disappear)

        if (throwsError) {
            val requiredErrorShould = assertThrows<RequiredError> {
                myMainPage.shouldLoadRequired()
            }
            assertEquals(1, requiredErrorShould.suppressed.size)
            assertFalse { myMainPage.hasLoadedRequired() }
        } else {
            myMainPage.shouldLoadRequired()
        }
    }

    @Test
    fun overrideRequiredTest() {
        open class BadMainPage : MainPage() {
            @Required open val badSelector = find("no-exists")
        }

        val myMainPage = object : BadMainPage() {
            override val badSelector = super.badSelector
        }

        setupSite("chrome", "es")
        myMainPage.shouldLoadRequired()
    }

    @Test
    fun widgetFindTest() {
        val myMainPage = object : MainPage() {
            inner class Body(self: SelenideElement) : Widget(self) {
                @Required val storyRiver1 = findX(".//section[contains(@class,'tc-story-river')]", 0)
                @Required val storyRivers1 = findXAll(".//section[contains(@class,'tc-story-river')]")
                @Required val storyRiver2 = find(By.xpath(".//section[contains(@class,'tc-story-river')]"), 0)
                @Required val storyRivers2 = findAll(By.xpath(".//section[contains(@class,'tc-story-river')]"))
            }

            @Required val body = Body(find("body"))
        }
        setupSite("chrome", "es")
        myMainPage.shouldLoadRequired()
    }
}
