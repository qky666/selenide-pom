package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom

import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar.SidebarWidget

open class MainPage : Page() {
    @Required val storyRiverWidget = StoryRiverWidget(element("section.tc-story-river"))

    val hideSidebar = find("button.tc-hide-sidebar-btn")
    val showSidebar = find("button.tc-show-sidebar-btn")

    val sidebarWidget = SidebarWidget(find("div.tc-sidebar-scrollable"))
}

class StoryRiverWidget(self: SelenideElement) : Widget(self)

@Suppress("unused")
val mainPage = MainPage()
