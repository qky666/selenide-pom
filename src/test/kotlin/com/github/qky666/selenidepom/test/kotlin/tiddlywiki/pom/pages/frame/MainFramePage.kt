package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.pages.frame

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

open class MainFramePage : Page() {
    @Required val storyRiverWidget = StoryRiverWidget(element("section.tc-story-river"))

    val hideSidebar = find("button.tc-hide-sidebar-btn")
    val showSidebar = find("button.tc-show-sidebar-btn")

    val sidebarWidget = SidebarWidget(find("div.tc-sidebar-scrollable"))
}

class StoryRiverWidget(self: SelenideElement) : Widget(self) {}

class SidebarWidget(self: SelenideElement) : Widget(self) {
    @Required val title = ConditionedElement(
        find("h1.tc-site-title"), mapOf(
            "en" to Condition.exactText("My TiddlyWiki"),
            "es" to Condition.exactText("Mi TiddlyWiki")
        )
    )

    @Required val subtitle = ConditionedElement(
        find(".tc-site-subtitle"), mapOf(
            "en" to Condition.exactText("a non-linear personal web notebook"),
            "es" to Condition.exactText("Cuaderno de notas personal no-lineal en la web")
        )
    )

    @Required val newTiddler = findX(".//button[contains(@class,'new-tiddler')]")
    @Required val controlPanel = findX(".//button[contains(@class,'control-panel')]")
    @Required val saveWiki = findX(".//button[contains(@class,'save-wiki')]")

    @Required val sidebarTabs = SidebarTabsWidget(find("div.tc-sidebar-tabs-main.tc-tab-set"))
}

class SidebarTabsWidget(self: SelenideElement) : Widget(self) {
    @Required val openTabButton = ConditionedElement(
        find("div.tc-tab-buttons button", 0), mapOf(
            "en" to Condition.exactText("Open"),
            "es" to Condition.exactText("Abiertos"),
        )
    )

    @Required val recentTabButton = ConditionedElement(
        find("div.tc-tab-buttons button", 1), mapOf(
            "en" to Condition.exactText("Recent"),
            "es" to Condition.exactText("Recientes"),
        )
    )

    @Required val toolsTabButton = ConditionedElement(
        find("div.tc-tab-buttons button", 2), mapOf(
            "en" to Condition.exactText("Tools"),
            "es" to Condition.exactText("Herramientas"),
        )
    )

    @Required val moreTabButton = ConditionedElement(
        find("div.tc-tab-buttons button", 3), mapOf(
            "en" to Condition.exactText("More"),
            "es" to Condition.exactText("Más"),
        )
    )

    val openTabContent = OpenTabContentWidget(find("div.tc-tab-content div.tc-reveal", 0))
}

class OpenTabContentWidget(self: SelenideElement) : Widget(self) {

}

@Suppress("unused") val mainFramePage = MainFramePage()
