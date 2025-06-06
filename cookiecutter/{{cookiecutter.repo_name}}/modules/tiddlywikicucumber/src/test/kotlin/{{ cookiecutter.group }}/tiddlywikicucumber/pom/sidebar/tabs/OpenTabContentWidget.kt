package {{ cookiecutter.group }}.tiddlywikicucumber.pom.sidebar.tabs

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection

class OpenTabContentWidget(self: SelenideElement) : Widget(self) {
    val openItems = WidgetsCollection(findAll("div.tc-sidebar-tab-open-item"), ::OpenItemWidget)

    @Required val closeAll = LangConditionedElement(
        find("div.tc-sidebar-tab-open>div:last-child button"), mapOf("eng" to "close all", "spa" to "Cerrar todo")
    )
}

class OpenItemWidget(self: SelenideElement) : Widget(self) {
    @Required val close = find("button")
    @Required val title = find("a")
}
