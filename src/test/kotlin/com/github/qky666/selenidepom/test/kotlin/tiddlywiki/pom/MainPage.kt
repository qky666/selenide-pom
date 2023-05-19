package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom

import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.popup.SearchPopupWidget
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar.SidebarWidget
import com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.storyriver.StoryRiverWidget
import org.openqa.selenium.By
import java.time.Duration

open class MainPage : Page() {
    // @Required val storyRiverWidget = StoryRiverWidget(find("section.tc-story-river"))
    @Required val storyRiver = StoryRiverWidget(findAll(By.cssSelector("section.tc-story-river"))[0])

    val hideSidebar = find("button.tc-hide-sidebar-btn")

    // val showSidebar = find("button.tc-show-sidebar-btn")
    val showSidebar = findX(".//button[contains(@class,'tc-show-sidebar-btn')]", 0)

    // @Required val hideShowSidebar = find("button.tc-hide-sidebar-btn, button.tc-show-sidebar-btn")
    @Required open val hideShowSidebar =
        findX(".//button[contains(@class,'tc-hide-sidebar-btn') or contains(@class,'tc-show-sidebar-btn')]")

    //    val sidebar = SidebarWidget(find("div.tc-sidebar-scrollable"))
    val sidebar = SidebarWidget(findAll("div.tc-sidebar-scrollable div.tc-sidebar-header>div.tc-reveal")[0])

    open val searchPopup = SearchPopupWidget(find("div.tc-search-drop-down"))

    override fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
        super.customShouldLoadRequired(timeout, model, lang)
        if (hideSidebar.isDisplayed) {
            sidebar.shouldLoadRequired(timeout, model, lang)
        }
    }
}

val mainPage = MainPage()
