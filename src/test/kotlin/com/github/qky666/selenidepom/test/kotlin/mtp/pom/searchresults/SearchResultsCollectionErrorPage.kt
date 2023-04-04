package com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.WidgetsCollection
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.BreadCrumbWidget
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.frame.MainFramePage

class SearchResultsCollectionErrorPage : MainFramePage() {
    @Required val breadcrumb = BreadCrumbWidget(element("ul.uk-breadcrumb"))

    // Possible bug in mtp.es, message is always displayed in spanish
    @Required val title =
        ConditionedElement(elements("h2.uk-article-title").first(), Condition.text("Resultados de búsqueda para:"))

    @Suppress("unused")
    val searchResults = elements("article[id]")

    @Required val searchResultsError = WidgetsCollection(elements("article_bad[id]"), ::SearchResultItemWidget)
}

val searchResultsCollectionErrorPage = SearchResultsCollectionErrorPage()
