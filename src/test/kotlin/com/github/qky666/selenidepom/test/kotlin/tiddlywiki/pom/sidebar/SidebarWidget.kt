package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class SidebarWidget(self: SelenideElement) : Widget(self) {
    @Required val title = ConditionedElement(
        find("h1.tc-site-title"), mapOf(
            "en" to "My TiddlyWiki", "es" to "Mi TiddlyWiki"
        )
    )

    @Required val subtitle = ConditionedElement(
        find(".tc-site-subtitle"), mapOf(
            "en" to "a non-linear personal web notebook", "es" to "Cuaderno de notas personal no-lineal en la web"
        )
    )

    @Required val newTiddler = find("button[class*=new-tiddler]")

    @Required val controlPanel = find("button[class*=control-panel]")

    @Required val saveWiki = find("button[class*=save-wiki]")

    @Required val searchInput = find("div.tc-search input")

    @Required val advancedSearch = find("div.tc-search button>svg.tc-image-advanced-search-button")
    val resetSearch = find("div.tc-search button>svg.tc-image-close-button")
    val searchResults = find("div.tc-search button[aria-expanded]")
    val searchResultsText = searchResults.find("small")

    @Required val sidebarTabs = SidebarTabsWidget(find("div.tc-sidebar-tabs-main.tc-tab-set"))
}

class SidebarTabsWidget(self: SelenideElement) : Widget(self) {
    @Required val openTabButton = ConditionedElement(
        find("div.tc-tab-buttons button", 0), mapOf(
            "en" to "Open", "es" to "Abiertos"
        )
    )

    @Required val recentTabButton = ConditionedElement(
        find("div.tc-tab-buttons button", 1), mapOf(
            "en" to "Recent", "es" to "Recientes"
        )
    )

    @Required val toolsTabButton = ConditionedElement(
        find("div.tc-tab-buttons button", 2), mapOf(
            "en" to "Tools", "es" to "Herramientas"
        )
    )

    @Required val moreTabButton = ConditionedElement(
        find("div.tc-tab-buttons button", 3), mapOf(
            "en" to "More", "es" to "Más"
        )
    )

    val openTabContent = OpenTabContentWidget(find("div.tc-tab-content div.tc-reveal", 0))
    val recentTabContent = RecentTabContentWidget(find("div.tc-tab-content div.tc-reveal", 1))
    val toolsTabContent = ToolsTabContentWidget(find("div.tc-tab-content div.tc-reveal", 2))
}
