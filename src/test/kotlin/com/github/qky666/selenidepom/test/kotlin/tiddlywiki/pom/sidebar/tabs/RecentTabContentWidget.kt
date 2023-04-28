package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar.tabs

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.common.Required
import com.github.qky666.selenidepom.pom.web.Widget
import com.github.qky666.selenidepom.pom.web.WidgetsCollection

class RecentTabContentWidget(self: SelenideElement) : Widget(self) {
    val dateItems = WidgetsCollection(findAll("div.tc-menu-list-item"), ::DateItemWidget)
}

class DateItemWidget(self: SelenideElement) : Widget(self) {
    @Required val items = find("div.tc-menu-list-item")
}
