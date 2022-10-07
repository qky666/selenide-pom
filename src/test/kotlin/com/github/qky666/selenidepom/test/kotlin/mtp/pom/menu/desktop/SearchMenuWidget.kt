package com.github.qky666.selenidepom.test.kotlin.mtp.pom.menu.desktop

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class SearchMenuWidget(self: SelenideElement) : Widget(self) {
    @Required val searchInput = find("input[name=s]")
    @Required val doSearch = find("button.search-submit")
}