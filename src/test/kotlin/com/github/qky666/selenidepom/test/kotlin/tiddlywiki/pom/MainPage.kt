package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom

import com.codeborne.selenide.Selenide.element
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar.SidebarWidget
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.storyriver.StoryRiverWidget
import java.time.Duration

open class MainPage : Page() {
    @Required val storyRiverWidget = StoryRiverWidget(element("section.tc-story-river"))

    val hideSidebar = find("button.tc-hide-sidebar-btn")
    val showSidebar = find("button.tc-show-sidebar-btn")
    @Required val hideShowSidebar = find("button[class$=sidebar-btn]")

    val sidebar = SidebarWidget(find("div.tc-sidebar-scrollable"))

    override fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
        super.customShouldLoadRequired(timeout, model, lang)
        if (hideSidebar.isDisplayed) {
            sidebar.shouldLoadRequired(timeout, model, lang)
        }
    }
}

@Suppress("unused")
val mainPage = MainPage()
