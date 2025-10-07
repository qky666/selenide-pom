package com.github.qky666.selenidepom.test.kotlin.tiddlywiki

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.exactValue
import com.codeborne.selenide.Condition.not
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Container
import com.codeborne.selenide.DragAndDropOptions
import com.codeborne.selenide.HoverOptions
import com.codeborne.selenide.ScrollIntoViewOptions
import com.codeborne.selenide.ScrollIntoViewOptions.Block
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.SetValueOptions
import com.codeborne.selenide.ex.ElementNotFound
import com.codeborne.selenide.ex.ElementShould
import com.github.qky666.selenidepom.condition.ImageCondition.containsImage
import com.github.qky666.selenidepom.condition.OcrTextCondition.exactOcrText
import com.github.qky666.selenidepom.condition.LangCondition.langCondition
import com.github.qky666.selenidepom.condition.OcrTextCondition.ocrText
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.ResourceHelper.Companion.getResourcePath
import com.github.qky666.selenidepom.data.ResourceHelper.Companion.getResourcePathString
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.ByImage
import com.github.qky666.selenidepom.pom.ConditionNotDefinedError
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.RequiredError
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection
import com.github.qky666.selenidepom.pom.appendToWidget
import com.github.qky666.selenidepom.pom.asWidget
import com.github.qky666.selenidepom.pom.clickImage
import com.github.qky666.selenidepom.pom.clickOcrText
import com.github.qky666.selenidepom.pom.clickWidget
import com.github.qky666.selenidepom.pom.contains
import com.github.qky666.selenidepom.pom.contextClickWidget
import com.github.qky666.selenidepom.pom.doubleClickWidget
import com.github.qky666.selenidepom.pom.dragWidgetAndDrop
import com.github.qky666.selenidepom.pom.hasLoadedRequired
import com.github.qky666.selenidepom.pom.hoverWidget
import com.github.qky666.selenidepom.pom.isContainedIn
import com.github.qky666.selenidepom.pom.ocrText
import com.github.qky666.selenidepom.pom.pressEnterInWidget
import com.github.qky666.selenidepom.pom.pressEscapeInWidget
import com.github.qky666.selenidepom.pom.pressTabInWidget
import com.github.qky666.selenidepom.pom.scrollToWidget
import com.github.qky666.selenidepom.pom.scrollWidgetIntoView
import com.github.qky666.selenidepom.pom.setSelectedWidget
import com.github.qky666.selenidepom.pom.setWidgetValue
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.pom.shouldMeetCondition
import com.github.qky666.selenidepom.pom.widgetAs
import com.github.qky666.selenidepom.pom.widgetShould
import com.github.qky666.selenidepom.pom.widgetShouldBe
import com.github.qky666.selenidepom.pom.widgetShouldHave
import com.github.qky666.selenidepom.pom.widgetShouldNot
import com.github.qky666.selenidepom.pom.widgetShouldNotBe
import com.github.qky666.selenidepom.pom.widgetShouldNotHave
import com.github.qky666.selenidepom.pom.widgetVal
import com.github.qky666.selenidepom.test.kotlin.downloadTiddlywikiEs
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.MainPage
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.storyriver.ControlPanelTiddlerViewWidget
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.storyriver.GettingStartedTiddlerViewWidget
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import java.net.URL
import java.time.Duration
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TiddlywikiTest {
    class FakeWidget(self: SelenideElement) : Widget(self)

    companion object {
        private val logger = KotlinLogging.logger {}

        @JvmStatic
        fun browserConfigAndLangSource() = listOf(
            Arguments.of("chrome", "spa"),
            Arguments.of("chrome", "eng"),
            Arguments.of("firefox", "spa"),
            Arguments.of("firefox", "eng"),
            Arguments.of("chromeMobile", "spa"),
            Arguments.of("chromeMobile", "eng"),
        )

        @JvmStatic
        fun desktopBrowserConfigAndLangSource() = listOf(
            Arguments.of("chrome", "spa"),
            Arguments.of("chrome", "eng"),
            Arguments.of("firefox", "spa"),
            Arguments.of("firefox", "eng"),
        )

        @JvmStatic
        fun requiredSource() = listOf(Arguments.of("chrome", "spa", true, object : MainPage() {
            @Required override val searchPopup = super.searchPopup
        }), Arguments.of("chrome", "eng", true, object : MainPage() {
            @Required val noExists = find(By.cssSelector("no-exists"))
        }), Arguments.of("firefox", "spa", true, object : MainPage() {
            @Required val noExistsCollection = findAll("no-exists")
            @Required val doExistsCollection = findAll("body")
        }), Arguments.of("firefox", "eng", true, object : MainPage() {
            @Required val noExistsBy = By.cssSelector("no-exists")
            @Required val doExistsBy = By.cssSelector("body")
        }), Arguments.of("chromeMobile", "eng", true, object : MainPage() {
            inner class MyWidget(self: SelenideElement) : Widget(self) {
                @Required val noExists = find("no-exists")
            }

            @Required val widgetsCollection = WidgetsCollection(findAll("body"), ::MyWidget)

            @Suppress("unused") val notRequiredWidgetsCollection = WidgetsCollection(findAll("body"), ::MyWidget)
        }), Arguments.of("firefox", "spa", true, object : MainPage() {
            inner class MyWidget(self: SelenideElement) : Widget(self) {
                @Required val doExists = find("body")
            }

            @Required val widgetsCollection = WidgetsCollection(findAll("no-exists"), ::MyWidget)
        }), Arguments.of("chrome", "spa", true, object : MainPage() {
            @Required
            fun getParameterElement(
                model: String = SPConfig.model,
                lang: String = SPConfig.lang,
            ): SelenideElement {
                return find("no-exists-$model-$lang")
            }
        }), Arguments.of("chrome", "eng", true, object : MainPage() {
            @Required
            fun getElement(): SelenideElement {
                return find("no-exists")
            }
        }), Arguments.of("chrome", "spa", false, object : MainPage() {
            @Required(model = "mobile") val noExistsModel = find("no-exists")
        }), Arguments.of("firefox", "eng", false, object : MainPage() {
            @Required(model = "mobile") val noExistsModel = find("no-exists")
        }), Arguments.of("chromeMobile", "spa", true, object : MainPage() {
            @Required(model = "mobile") val noExistsModel = find("no-exists")
        }), Arguments.of("chrome", "spa", true, object : MainPage() {
            @Required(lang = "spa") val noExistsLang = find("no-exists")
        }), Arguments.of("firefox", "eng", false, object : MainPage() {
            @Required(lang = "spa") val noExistsLang = find("no-exists")
        }), Arguments.of("chromeMobile", "spa", true, object : MainPage() {
            @Required(lang = "spa") val noExistsLang = find("no-exists")
        }), Arguments.of("firefox", "spa", true, object : MainPage() {
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
        }), Arguments.of("firefox", "eng", true, object : MainPage() {
            @Required val langConditionedElement = LangConditionedElement(find("no-exists"), "NoText")
        }), Arguments.of("chrome", "spa", true, object : MainPage() {
            @Required val langConditionedElement =
                LangConditionedElement(super.hideShowSidebar, mapOf("spa" to "BadText"))
        }), Arguments.of("chrome", "eng", true, object : MainPage() {
            @Required val langConditionedElement =
                LangConditionedElement(super.hideShowSidebar, mapOf("spa" to "BadText"))
        }), Arguments.of("firefox", "spa", true, object : MainPage() {
            @Required val langConditionedElement =
                LangConditionedElement(super.hideShowSidebar, mapOf("spa" to exactText("BadText")), false)
        }), Arguments.of("firefox", "eng", false, object : MainPage() {
            @Required val langConditionedElement =
                LangConditionedElement(super.hideShowSidebar, mapOf("spa" to exactText("BadText")), false)
        }), Arguments.of("chrome", "spa", true, object : MainPage() {
            @Required val badContainer = object : Container {
                @Required val noExists = find("no-exists")
            }
            @Required val goodContainer = object : Container {
                @Required val body = find("body")
            }
        }))

//        @JvmStatic
//        @BeforeAll
//        fun beforeAll() {
//            url = downloadTiddlywikiEs().toURI().toURL()
//        }
    }

    @BeforeEach
    fun beforeEach() {
        SPConfig.resetConfig()
        TestData.init("tiddlywiki-prod")

        // Additional test for output in TestData
        TestData.set("threadId", Thread.currentThread().threadId())
    }

    @AfterEach
    fun afterEach() {
        SPConfig.quitDriver()
        // Additional test for output in TestData
        Assertions.assertEquals(TestData.get("threadId").toString(), Thread.currentThread().threadId().toString())
    }

    private fun setupSite(browserConfig: String, lang: String = "spa") {
        if (browserConfig.equals("chromeMobile", ignoreCase = true)) SPConfig.setupBasicMobileBrowser()
        else SPConfig.setupBasicDesktopBrowser(browserConfig)
        SPConfig.setDriver()
        SPConfig.lang = lang
        Page.load(MainPage::class)
        changeSiteLanguageIfNeeded()
    }

    private fun changeSiteLanguageIfNeeded(newLang: String = SPConfig.lang, currentLang: String = "spa") {
        if (!newLang.contentEquals(currentLang, true)) {
            val mainPage = Page.getInstance(MainPage::class)
            if (mainPage.shouldLoadRequired(lang = currentLang).showSidebar.isDisplayed) {
                mainPage.showSidebar.click()
                mainPage.sidebar.shouldLoadRequired(lang = currentLang)
            }
            val sidebarTabs = mainPage.sidebar.sidebarTabs
            sidebarTabs.toolsTabButton.click()
            sidebarTabs.toolsTabContent.shouldLoadRequired(lang = currentLang).language.button.click()
            sidebarTabs.toolsTabContent.languageChooser.shouldLoadRequired(lang = currentLang)
            if (newLang.contentEquals("spa", true)) sidebarTabs.toolsTabContent.languageChooser.esES.click()
            else sidebarTabs.toolsTabContent.languageChooser.enGB.click()
            sidebarTabs.toolsTabContent.languageChooser.should(disappear)
            mainPage.shouldLoadRequired(lang = newLang)
            sidebarTabs.openTabButton.click()
            mainPage.shouldLoadRequired(lang = newLang)
        }
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun verifyGettingStartedAndCloseAllTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)
        val mainPage = Page.getInstance(MainPage::class)
        val firstTiddler = mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
        // GettingStartedTiddlerViewWidget(firstTiddler).shouldLoadRequired()
        GettingStartedTiddlerViewWidget(Page.findAll(listOf(firstTiddler.toWebElement()))[0]).shouldLoadRequired()

        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(1))
        mainPage.sidebar.sidebarTabs.openTabContent.closeAll.click(ClickOptions.usingJavaScript())
        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
        mainPage.storyRiver.tiddlerViews.shouldHave(size(0))
        mainPage.storyRiver.tiddlerEdits.shouldHave(size(0))
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun verifySidebarTabsTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)

        // Populate the Recent tab.
        // newTiddler works better with JavaScript click
        val mainPage = Page.getInstance(MainPage::class)
        mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
        mainPage.storyRiver.tiddlerEdits.shouldHave(size(1))

        mainPage.sidebar.sidebarTabs.tabButtonToTabContentMap.forEach { (tabButton, tabContent) ->
            tabButton.click()
            tabContent.shouldLoadRequired(timeout = Duration.ofSeconds(10))
        }

        mainPage.sidebar.sidebarTabs.recentTabButton.click()
        mainPage.sidebar.sidebarTabs.recentTabContent.shouldLoadRequired().dateItems.shouldHave(size(1))
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun showHideSidebarTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)
        val mainPage = Page.getInstance(MainPage::class)
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

        // newTiddler works better with JavaScript click
        val mainPage = Page.getInstance(MainPage::class)
        mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
        val newTiddlerEdit = mainPage.storyRiver.tiddlerEdits.shouldHave(size(1))[0].shouldLoadRequired()
        newTiddlerEdit.titleInput.value = newTiddlerTitle
        // Selenide helpers for shadow dom not working here (do not know why), so we do it the hard way with switchTo
        val webdriver = SPConfig.getWebDriver()!!
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
        val searchResultsText = mainPage.sidebar.searchResultsText
        searchResultsText.shouldBe(langCondition(mapOf("eng" to "1 matches", "spa" to "1 coincidencias")))

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

        // newTiddler works better with JavaScript click
        val mainPage = Page.getInstance(MainPage::class)
        mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
        val newTiddlerEdit = mainPage.storyRiver.tiddlerEdits.shouldHave(size(1))[0].shouldLoadRequired()
        newTiddlerEdit.titleInput.value = newTiddlerTitle
        // Selenide helpers for shadow dom not working here (do not know why), so we do it the hard way with switchTo
        val webdriver = SPConfig.getWebDriver()!!
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
        val searchResultsText = mainPage.sidebar.searchResultsText
        searchResultsText.shouldBe(langCondition(mapOf("eng" to "1 matches", "spa" to "1 coincidencias")))

        // Incomplete LangCondition (non strict)
        searchResultsText.shouldBe(langCondition(mapOf("eng" to exactText("1 matches")), false))

        // Incomplete LangCondition
        val incompleteLangCondition = langCondition(mapOf("eng" to exactText("1 matches")))
        if (SPConfig.lang == "spa") {
            assertThrows<ConditionNotDefinedError> { searchResultsText.shouldBe(incompleteLangCondition) }
        } else searchResultsText.shouldBe(incompleteLangCondition)

        firstSearchResult.click()

        val tiddlerSearchResult = mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0]
        tiddlerSearchResult.title.shouldHave(exactText(newTiddlerTitle))
        tiddlerSearchResult.body.shouldHave(exactText(newTiddlerBody))
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun shouldMeetConditionTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)
        val firstTiddler =
            Page.getInstance(MainPage::class).storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
        val gettingStartedTiddler = GettingStartedTiddlerViewWidget(firstTiddler).shouldLoadRequired()
        gettingStartedTiddler.title.shouldMeetCondition()
        Assertions.assertTrue(gettingStartedTiddler.title.meetsCondition())

        val nonExistingElement = LangConditionedElement(
            Page.find("non-existing"), mapOf("eng" to exactText("Non existing")), false
        )
        Assertions.assertEquals(SPConfig.lang == "spa", nonExistingElement.meetsCondition())
        if (SPConfig.lang == "spa") nonExistingElement.shouldMeetCondition(Duration.ZERO)
        else assertThrows<ElementNotFound> { nonExistingElement.shouldMeetCondition(Duration.ZERO) }

        val strict = LangConditionedElement(Page.find("non-existing"), mapOf("eng" to "Non existing"))
        Assertions.assertEquals(false, strict.meetsCondition())
        if (SPConfig.lang == "spa") assertThrows<ConditionNotDefinedError> { strict.shouldMeetCondition(Duration.ZERO) }
        else assertThrows<ElementNotFound> { strict.shouldMeetCondition(Duration.ZERO) }
    }

    @ParameterizedTest
    @MethodSource("browserConfigAndLangSource")
    fun failedCustomShouldLoadRequiredTest(browserConfig: String, lang: String) {
        val myMainPage = object : MainPage() {
            override fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
                super.customShouldLoadRequired(timeout, model, lang)
                if (model.equals("mobile", true) or lang.equals("spa", true)) {
                    find("no-exists").shouldBe(visible, Duration.ZERO)
                }
            }
        }

        setupSite(browserConfig, lang)

        if (browserConfig.equals("chromeMobile", true) or lang.equals("spa", true)) {
            val requiredError = assertThrows<RequiredError> { myMainPage.shouldLoadRequired() }
            assertEquals(1, requiredError.suppressed.size)
        } else myMainPage.shouldLoadRequired()
    }

    @ParameterizedTest
    @MethodSource("requiredSource")
    fun requiredTest(browserConfig: String, lang: String, throwsError: Boolean, myMainPage: MainPage) {
        setupSite(browserConfig, lang)
        myMainPage.hideSidebar.click()
        myMainPage.showSidebar.shouldBe(visible)
        myMainPage.sidebar.should(disappear)

        if (throwsError) {
            val requiredErrorShould = assertThrows<RequiredError> { myMainPage.shouldLoadRequired() }
            assertEquals(1, requiredErrorShould.suppressed.size)
            assertFalse { myMainPage.hasLoadedRequired() }
        } else myMainPage.shouldLoadRequired()
    }

    @Test
    fun overrideRequiredTest() {
        open class BadMainPage : MainPage() {
            @Required open val badSelector = find("no-exists")
        }

        val myMainPage = object : BadMainPage() {
            override val badSelector = super.badSelector
        }

        setupSite("chrome")
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
        setupSite("chrome")
        myMainPage.shouldLoadRequired()
    }

    @Test
    fun widgetShouldValueTest() {
        val searchString = "SearchString"
        val appendString = "Append"
        val searchCondition = exactValue(searchString)
        val appendCondition = exactValue(searchString + appendString)
        val emptyCondition = exactValue("")
        val timeout = Duration.ofMillis(100)

        val mainPage = Page.getInstance(MainPage::class)
        val fake = mainPage.sidebar.searchInput.asWidget { FakeWidget(it) }

        setupSite("chrome")
        mainPage.shouldLoadRequired()

        fake.setWidgetValue(searchString)
            .widgetShould(searchCondition)
            .widgetShould(searchCondition, timeout)
            .widgetShouldBe(searchCondition)
            .widgetShouldBe(searchCondition, timeout)
            .widgetShouldHave(searchCondition)
            .widgetShouldHave(searchCondition, timeout)
            .widgetShouldNot(emptyCondition)
            .widgetShouldNot(emptyCondition, timeout)
            .widgetShouldNotBe(emptyCondition)
            .widgetShouldNotBe(emptyCondition, timeout)
            .widgetShouldNotHave(emptyCondition)
            .widgetShouldNotHave(emptyCondition, timeout)
            .clear()
        fake.widgetShould(emptyCondition).widgetShould(emptyCondition, timeout)

        fake.setWidgetValue(SetValueOptions.withText(searchString)).widgetShouldHave(searchCondition).clear()
        fake.widgetShouldHave(emptyCondition)

        fake.widgetVal(searchString).widgetShouldHave(searchCondition).clear()
        fake.widgetShouldHave(emptyCondition)

        fake.appendToWidget(searchString)
            .widgetShouldHave(searchCondition)
            .appendToWidget(appendString)
            .widgetShouldHave(appendCondition)
            .clear()
        fake.widgetShouldHave(emptyCondition)

        fake.pressEnterInWidget().pressEscapeInWidget().pressTabInWidget()
        fake.widgetShouldHave(emptyCondition)

        assertThrows<ElementShould> { fake.widgetShould(searchCondition) }
    }

    @Test
    fun widgetSelectedTest() {
        val mainPage = Page.getInstance(MainPage::class)
        val fake = mainPage.sidebar.sidebarTabs.toolsTabContent.home.checkbox.asWidget { FakeWidget(it) }
        setupSite("chrome")
        mainPage.shouldLoadRequired()
        mainPage.sidebar.sidebarTabs.toolsTabButton.click()
        mainPage.sidebar.sidebarTabs.toolsTabContent.shouldLoadRequired()
        assertTrue { fake.setSelectedWidget(true).isSelected }
        assertFalse { fake.setSelectedWidget(false).isSelected }
    }

    @Test
    fun widgetAsTest() {
        val mainPage = Page.getInstance(MainPage::class)
        val fake = mainPage.sidebar.searchInput.asWidget { FakeWidget(it) }
        val alias = "fake"
        assertEquals(alias, fake.widgetAs(alias).alias)
    }

    @Test
    fun widgetScrollTest() {
        val mainPage = Page.getInstance(MainPage::class)
        val fake = mainPage.sidebar.sidebarTabs.toolsTabContent.more.button.asWidget { FakeWidget(it) }
        setupSite("chrome")
        mainPage.shouldLoadRequired()
        mainPage.sidebar.sidebarTabs.toolsTabButton.click()
        mainPage.sidebar.sidebarTabs.toolsTabContent.shouldLoadRequired()
        fake.scrollToWidget()
            .scrollWidgetIntoView(ScrollIntoViewOptions.instant().block(Block.start))
            .scrollWidgetIntoView(ScrollIntoViewOptions.instant().block(Block.end))
    }

    @Test
    fun selenideElementScrollIntoCenterTest() {
        setupSite("chrome")
        val mainPage = Page.getInstance(MainPage::class).shouldLoadRequired()
        mainPage.sidebar.sidebarTabs.toolsTabButton.scrollIntoCenter().click()
        mainPage.sidebar.sidebarTabs.toolsTabContent.shouldLoadRequired()
    }

    @Test
    fun scrollToBottomTest() {
        setupSite("chrome")
        val mainPage = Page.getInstance(MainPage::class).shouldLoadRequired()
        mainPage.sidebar.sidebarTabs.toolsTabButton.scrollIntoCenter().click()
        mainPage.sidebar.sidebarTabs.toolsTabContent.shouldLoadRequired()

        for (i in 1..5) {
            mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
            mainPage.storyRiver.tiddlerEdits.shouldHave(size(i))[0].shouldLoadRequired()
            mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
        }
        assertTrue { mainPage.scrollToBottom() }
        assertTrue { mainPage.scrollToBottom(loadRequired = true) }
        mainPage.shouldLoadRequired()
    }

    @Test
    fun widgetClicksTest() {
        val mainPage = Page.getInstance(MainPage::class)
        val fake = mainPage.sidebar.searchInput.asWidget { FakeWidget(it) }
        setupSite("chrome")
        mainPage.shouldLoadRequired()
        fake.clickWidget(ClickOptions.usingDefaultMethod()).doubleClickWidget().contextClickWidget()
    }

    @Test
    fun widgetHoverTest() {
        val mainPage = Page.getInstance(MainPage::class)
        val fake = mainPage.sidebar.searchInput.asWidget { FakeWidget(it) }
        setupSite("chrome")
        mainPage.shouldLoadRequired()
        fake.hoverWidget().hoverWidget(HoverOptions.withOffset(20, 30))
    }

    @Test
    fun widgetDragAndDropTest() {
        val mainPage = Page.getInstance(MainPage::class)
        val fake = mainPage.sidebar.searchInput.asWidget { FakeWidget(it) }
        setupSite("chrome")
        mainPage.shouldLoadRequired()
        fake.dragWidgetAndDrop(DragAndDropOptions.to(mainPage.sidebar.newTiddler))
    }

    @Test
    fun findWidgetsCollectionTest() {
        setupSite("chrome")
        val mainPage = Page.getInstance(MainPage::class).shouldLoadRequired()
        val open = mainPage.sidebar.sidebarTabs.openTabContent.openItems
        open.find(visible).shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
        open.findBy(visible).shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
    }

    @Test
    fun firstWidgetsCollectionTest() {
        setupSite("chrome")
        val mainPage = Page.getInstance(MainPage::class).shouldLoadRequired()
        val open = mainPage.sidebar.sidebarTabs.openTabContent.openItems
        open.first().shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
        open.first(1)[0].shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
    }

    @Test
    fun lastWidgetsCollectionTest() {
        setupSite("chrome")
        val mainPage = Page.getInstance(MainPage::class).shouldLoadRequired()
        val open = mainPage.sidebar.sidebarTabs.openTabContent.openItems
        open.last().shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
        open.last(1)[0].shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
    }

    @Test
    fun shouldWidgetsCollectionTest() {
        setupSite("chrome")
        val mainPage = Page.getInstance(MainPage::class).shouldLoadRequired()
        val open = mainPage.sidebar.sidebarTabs.openTabContent.openItems
        open.should(size(1))[0].shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
        open.should(size(1), Duration.ZERO)[0].shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
        open.shouldBe(size(1))[0].shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
        open.shouldBe(size(1), Duration.ZERO)[0].shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
        open.shouldHave(size(1))[0].shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
        open.shouldHave(size(1), Duration.ZERO)[0].shouldLoadRequired().title.shouldHave(exactText("GettingStarted"))
    }

    @Test
    fun filterWidgetsCollectionTest() {
        val mainPage = Page.getInstance(MainPage::class)
        setupSite("chrome")
        mainPage.shouldLoadRequired()
        val open = mainPage.sidebar.sidebarTabs.openTabContent.openItems
        open.filterBy(visible).shouldHave(size(1))
    }

    @Test
    fun excludeWidgetsCollectionTest() {
        val mainPage = Page.getInstance(MainPage::class)
        setupSite("chrome")
        mainPage.shouldLoadRequired()
        val open = mainPage.sidebar.sidebarTabs.openTabContent.openItems
        open.exclude(visible).shouldHave(size(0))
        open.excludeWith(visible).shouldHave(size(0))
    }

    @Test
    fun snapshotWidgetsCollectionTest() {
        val mainPage = Page.getInstance(MainPage::class)
        setupSite("chrome")
        mainPage.shouldLoadRequired()
        val open = mainPage.sidebar.sidebarTabs.openTabContent.openItems
        val snapshot = open.snapshot()
        mainPage.sidebar.sidebarTabs.openTabContent.closeAll.click(ClickOptions.usingJavaScript())
        mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
        snapshot.shouldHave(size(1))
    }

    @Test
    fun widgetsCollectionAsTest() {
        val open = Page.getInstance(MainPage::class).sidebar.sidebarTabs.openTabContent.openItems
        val alias = "open"
        assertEquals(alias, open.`as`(alias).collectionSource.alias.text)
    }

    @Test
    fun objectNotLoadableTest() {
        val myPage = object : Page() {
            @Required val myObject = object {}
        }
        setupSite("chrome")
        myPage.shouldLoadRequired()
    }

    @Test
    fun hasConditionTimeoutFalseTest() {
        val timeoutSeconds = 1L
        val myPage = object : Page() {
            val badElement = find("bad-element")
        }
        setupSite("chrome")
        val startTime = LocalDateTime.now()
        assertFalse { myPage.badElement.has(visible, Duration.ofSeconds(timeoutSeconds)) }
        assertTrue { startTime.plusSeconds(timeoutSeconds) < LocalDateTime.now() }
    }

    @Test
    fun hasConditionTimeoutTrueTest() {
        val myPage = object : Page() {
            val badElement = find("bad-element")
        }
        setupSite("chrome")
        val startTime = LocalDateTime.now()
        assertTrue { myPage.badElement.has(not(visible), Duration.ofSeconds(5)) }
        assertTrue { startTime.plusSeconds(3) > LocalDateTime.now() }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigAndLangSource")
    fun byImageTest(browserConfig: String, lang: String) {
        // SPConfig.selenideConfig.headless(false)
        setupSite(browserConfig, lang)
        val mainPage = Page.getInstance(MainPage::class)
        val cpImage = mainPage.sidebar.controlPanelImage
        val cp = mainPage.sidebar.controlPanel
        mainPage.shouldLoadRequired()
        cpImage.click()
        val controlPanelTiddler = mainPage.storyRiver.tiddlerViews.shouldHave(size(2))[0].shouldLoadRequired()
        ControlPanelTiddlerViewWidget(controlPanelTiddler).shouldLoadRequired()
        assertTrue { cpImage.rect.contains(cp.rect) }
        assertFalse { cpImage.rect.isContainedIn(cp.rect) }
        cpImage.findAll("button[class*=control-panel]").shouldHave(size(1))
        cpImage.findAll("non-existent").shouldHave(size(0))
        cpImage.find("button[class*=control-panel]").shouldBe(visible)
    }

    @Test
    fun byImageSimpleConstructor() {
        setupSite("chrome")
        val mainPage = Page.getInstance(MainPage::class).shouldLoadRequired()
        mainPage.storyRiver.findAll(ByImage(getResourcePathString("images/no_exists/image.png")!!)).shouldHave(size(0))
        val toolbar = mainPage.sidebar.find(ByImage(getResourcePathString("images/toolbar/toolbar.png")!!, 0.5))
        toolbar.clickImage(ClickOptions.withOffset(-30, 2))
        val newTiddlerEdit = mainPage.storyRiver.tiddlerEdits.shouldHave(size(1))[0].shouldLoadRequired()
        newTiddlerEdit.save.click()
        val newTiddlerView = mainPage.storyRiver.tiddlerViews.shouldHave(size(2))[0]
        newTiddlerView.title.shouldHave(exactText("Nuevo Tiddler"))
    }

    @Test
    fun imageCondition() {
        setupSite("chrome")
        val title = Page.find(ByImage.name("tiddlywiki-title"))
        val ocrTitle = title.ocrText()
        logger.info { "Title found using OCR: $ocrTitle" }
        assertEquals("Mi TiddlyWiki", ocrTitle)
        title.shouldHave(
            text("Mi TiddlyWiki"),
            exactOcrText("Mi TiddlyWiki"),
            ocrText("TiddlyWiki"),
            not(ocrText("Other text"))
        )
        val sidebarTabs = Page.getInstance(MainPage::class).sidebar.sidebarTabs
        sidebarTabs.toolsTabButton.click()
        sidebarTabs.toolsTabContent.shouldLoadRequired().language.button.click()
        sidebarTabs.toolsTabContent.languageChooser.shouldLoadRequired()
        sidebarTabs.shouldNotHave(containsImage(getResourcePath("images/no_exists/image.png")!!))
        sidebarTabs.shouldNotHave(
            containsImage(
                getResourcePath("images/no_exists/image.png")!!.toAbsolutePath().toString()
            )
        )
    }

    @Test
    fun clickOcrTextTest() {
        setupSite("chrome")
        val mainPage = Page.getInstance(MainPage::class).shouldLoadRequired()
        val firstTiddler = mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
        GettingStartedTiddlerViewWidget(firstTiddler).shouldLoadRequired().clickOcrText("panel de control")
        val controlPanelTiddler = mainPage.storyRiver.tiddlerViews.shouldHave(size(2))[1].shouldLoadRequired()
        ControlPanelTiddlerViewWidget(controlPanelTiddler).shouldLoadRequired()
    }
}
