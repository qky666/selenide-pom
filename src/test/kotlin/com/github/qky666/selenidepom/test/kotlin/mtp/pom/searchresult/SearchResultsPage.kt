package com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresult

import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.WidgetsCollection
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.BreadCrumbWidget
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.MainFramePage

class SearchResultsPage : MainFramePage() {
    @Required val breadcrumb = BreadCrumbWidget(element("ul.uk-breadcrumb"))

    @Required val title = elements("h2.uk-article-title").first()

    val searchResults = WidgetsCollection(elements("article[id]"), ::SearchResultItemWidget)
}

val searchResultsPage = SearchResultsPage()
