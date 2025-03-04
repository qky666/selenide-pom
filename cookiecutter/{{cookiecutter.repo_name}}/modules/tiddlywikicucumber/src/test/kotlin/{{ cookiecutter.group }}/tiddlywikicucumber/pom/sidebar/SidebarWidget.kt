package {{ cookiecutter.group }}.tiddlywikicucumber.pom.sidebar

import com.codeborne.selenide.Condition.cssClass
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import {{ cookiecutter.group }}.tiddlywikicucumber.pom.sidebar.tabs.MoreTabContentWidget
import {{ cookiecutter.group }}.tiddlywikicucumber.pom.sidebar.tabs.OpenTabContentWidget
import {{ cookiecutter.group }}.tiddlywikicucumber.pom.sidebar.tabs.RecentTabContentWidget
import {{ cookiecutter.group }}.tiddlywikicucumber.pom.sidebar.tabs.ToolsTabContentWidget
import java.time.Duration

@Suppress("MemberVisibilityCanBePrivate")
class SidebarWidget(self: SelenideElement) : Widget(self) {
    @Required val title = LangConditionedElement(
        find("h1.tc-site-title"), mapOf(
            "eng" to "My TiddlyWiki", "spa" to "Mi TiddlyWiki"
        )
    )

    @Required val subtitle = LangConditionedElement(
        find(".tc-site-subtitle"), mapOf(
            "eng" to "a non-linear personal web notebook", "spa" to "Cuaderno de notas personal no-lineal en la web"
        )
    )

    @Required val newTiddler = find("button[class*=new-tiddler]")

    @Required val controlPanel = find("button[class*=control-panel]")

    @Required val saveWiki = find("button[class*=save-wiki]")

    @Required val searchInput = find("div.tc-search input")

    @Required val advancedSearch = find("div.tc-search button>svg.tc-image-advanced-search-button")
    val resetSearch = find("div.tc-search button>svg.tc-image-close-button")
    val searchResultsButton = find("div.tc-search button[aria-expanded]")
    val searchResultsText = searchResultsButton.find("small")

    @Required val sidebarTabs = SidebarTabsWidget(find("div.tc-sidebar-tabs-main.tc-tab-set"))
}

@Suppress("MemberVisibilityCanBePrivate")
class SidebarTabsWidget(self: SelenideElement) : Widget(self) {
    @Required val selectedTabButton = find("div.tc-tab-buttons button.tc-tab-selected")

    @Required val openTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button", 0), mapOf(
            "eng" to "Open", "spa" to "Abiertos"
        )
    )

    @Required val recentTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button", 1), mapOf(
            "eng" to "Recent", "spa" to "Recientes"
        )
    )

    @Required val toolsTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button", 2), mapOf(
            "eng" to "Tools", "spa" to "Herramientas"
        )
    )

    @Required val moreTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button", 3), mapOf(
            "eng" to "More", "spa" to "Más"
        )
    )

    val openTabContent = OpenTabContentWidget(find("div.tc-tab-content div.tc-reveal", 0))
    val recentTabContent = RecentTabContentWidget(find("div.tc-tab-content div.tc-reveal", 1))
    val toolsTabContent = ToolsTabContentWidget(find("div.tc-tab-content div.tc-reveal", 2))
    val moreTabContent = MoreTabContentWidget(find("div.tc-tab-content div.tc-reveal", 3))

    val tabButtonToTabContentMap = mapOf(
        openTabButton to openTabContent,
        recentTabButton to recentTabContent,
        toolsTabButton to toolsTabContent,
        moreTabButton to moreTabContent
    )

    override fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
        super.customShouldLoadRequired(timeout, model, lang)
        tabButtonToTabContentMap.forEach { (tabButton, tabContent) ->
            if (tabButton.has(cssClass("tc-tab-selected"))) tabContent.shouldLoadRequired(timeout, model, lang)
        }
    }
}
