package com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresult

import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.BreadCrumbWidget
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.MainFramePage

class SearchResultsErrorPage : MainFramePage() {
    @Required val breadcrumb = BreadCrumbWidget(element("ul.uk-breadcrumb"))

    @Required val title = elements("h2.uk-article-title").first()

    val searchResultsError = WidgetsCollection(elements("article[id]"), ::SearchResultItemRequiredErrorWidget)
}

class SearchResultItemRequiredErrorWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("h2>a")
    val text = find("div.tm-article-content")

    @Required val badSelector = find("bad-selector")
}

val searchResultsErrorPage = SearchResultsErrorPage()