package com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults

import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.WidgetsCollection
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.BreadCrumbWidget
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.MainFramePage

class SearchResultsCollectionErrorPage : MainFramePage() {
    @Required val breadcrumb = BreadCrumbWidget(element("ul.uk-breadcrumb"))

    @Required val title = elements("h2.uk-article-title").first()

    @Required val searchResults = elements("article[id]")
    @Required val searchResultsError = WidgetsCollection(elements("article_bad[id]"), ::SearchResultItemWidget)
}

val searchResultsCollectionErrorPage = SearchResultsCollectionErrorPage()
