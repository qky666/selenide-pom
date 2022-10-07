package com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresult

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class SearchResultItemRequiredErrorWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("h2>a")
    val text = find("div.tm-article-content")

    @Required val badSelector = find("bad-selector")
}
