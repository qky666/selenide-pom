package com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.BreadCrumbWidget
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.frame.MainFramePage

class SearchResultsErrorPage : MainFramePage() {
    @Required val breadcrumb = BreadCrumbWidget(element("ul.uk-breadcrumb"))

    // Possible bug in mtp.es, message is always displayed in spanish
    @Required val title =
        ConditionedElement(elements("h2.uk-article-title").first(), Condition.text("Resultados de búsqueda para:"))

    val searchResultsError = WidgetsCollection(elements("article[id]"), ::SearchResultItemRequiredErrorWidget)
}

class SearchResultItemRequiredErrorWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("h2>a")
    val text = find("div.tm-article-content")

    @Required val badSelector = find("bad-selector")
}

val searchResultsErrorPage = SearchResultsErrorPage()
