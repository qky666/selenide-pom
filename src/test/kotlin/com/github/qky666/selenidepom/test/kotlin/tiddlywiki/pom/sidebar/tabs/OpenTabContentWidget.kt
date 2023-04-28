package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar.tabs

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.common.Required
import com.github.qky666.selenidepom.pom.web.LangConditionedElement
import com.github.qky666.selenidepom.pom.web.Widget
import com.github.qky666.selenidepom.pom.web.WidgetsCollection

class OpenTabContentWidget(self: SelenideElement) : Widget(self) {
    val openItems = WidgetsCollection(findAll("div.tc-sidebar-tab-open-item"), ::OpenItemWidget)

    @Required val closeAll = LangConditionedElement(
        find("div.tc-sidebar-tab-open>div:last-child button"),
        mapOf(
            "en" to "close all",
            "es" to "Cerrar todo"
        )
    )
}

class OpenItemWidget(self: SelenideElement) : Widget(self) {
    @Required val close = find("button")

    @Required val title = find("a")
}
