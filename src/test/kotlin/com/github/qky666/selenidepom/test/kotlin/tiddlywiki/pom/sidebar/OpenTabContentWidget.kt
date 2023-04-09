package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection

class OpenTabContentWidget(self: SelenideElement) : Widget(self) {
    val openItems = WidgetsCollection(findAll("div.tc-sidebar-tab-open-item"), ::OpenItemWidget)

    @Required val closeAll = ConditionedElement(
        find("div.tc-sidebar-tab-open>div:last-child button"),
        mapOf(
            "en" to Condition.exactText("close all"),
            "es" to Condition.exactText("Cerrar todo")
        )
    )
}

class OpenItemWidget(self: SelenideElement) : Widget(self) {
    @Required val close = find("button")

    @Required val title = find("a")
}
