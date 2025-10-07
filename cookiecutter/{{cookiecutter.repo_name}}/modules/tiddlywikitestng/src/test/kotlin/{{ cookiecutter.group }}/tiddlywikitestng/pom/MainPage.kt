package {{ cookiecutter.group }}.tiddlywikitestng.pom

import com.codeborne.selenide.Condition.disappear
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import {{ cookiecutter.group }}.tiddlywikitestng.pom.popup.SearchPopupWidget
import {{ cookiecutter.group }}.tiddlywikitestng.pom.sidebar.SidebarWidget
import {{ cookiecutter.group }}.tiddlywikitestng.pom.storyriver.StoryRiverWidget
import java.time.Duration

@Suppress("MemberVisibilityCanBePrivate")
class MainPage : Page() {
    override val url = TestData.getString("project.baseUrl")!!

    @Required(model = "desktop") val storyRiver = StoryRiverWidget(find("section.tc-story-river"))
    val hideSidebar = find("button.tc-hide-sidebar-btn")
    val showSidebar = find("button.tc-show-sidebar-btn")
    @Required val hideShowSidebar = find("button.tc-hide-sidebar-btn, button.tc-show-sidebar-btn")
    val sidebar = SidebarWidget(find("div.tc-sidebar-scrollable div.tc-sidebar-header>div.tc-reveal"))
    val searchPopup = SearchPopupWidget(find("div.tc-search-drop-down"))

    override fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
        super.customShouldLoadRequired(timeout, model, lang)
        if (hideSidebar.isDisplayed) sidebar.shouldLoadRequired(timeout, model, lang)
    }

    fun changeSiteLanguageIfNeeded(newLang: String = SPConfig.lang, currentLang: String = "spa") {
        if (!newLang.contentEquals(currentLang, true)) {
            if (shouldLoadRequired(lang = currentLang).showSidebar.isDisplayed) {
                showSidebar.click()
                sidebar.shouldLoadRequired(lang = currentLang)
            }
            sidebar.sidebarTabs.toolsTabButton.click()
            sidebar.sidebarTabs.toolsTabContent.shouldLoadRequired(lang = currentLang).language.button.click()
            sidebar.sidebarTabs.toolsTabContent.languageChooser.shouldLoadRequired(lang = currentLang)
            if (newLang.contentEquals("spa", true)) sidebar.sidebarTabs.toolsTabContent.languageChooser.esES.click()
            else sidebar.sidebarTabs.toolsTabContent.languageChooser.enGB.click()
            sidebar.sidebarTabs.toolsTabContent.languageChooser.should(disappear)
            shouldLoadRequired(lang = newLang)
            sidebar.sidebarTabs.openTabButton.click()
            shouldLoadRequired(lang = newLang)
        }
    }

    fun showHideSidebar(show: Boolean = true) {
        shouldLoadRequired()
        if (show && showSidebar.isDisplayed) {
            showSidebar.click()
            sidebar.shouldLoadRequired()
        } else if (!show && hideSidebar.isDisplayed) {
            hideSidebar.click()
            sidebar.should(disappear)
        }
    }
}
