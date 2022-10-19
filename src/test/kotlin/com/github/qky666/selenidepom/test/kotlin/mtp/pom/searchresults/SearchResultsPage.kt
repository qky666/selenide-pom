package com.github.qky666.selenidepom.test.kotlin.mtp.pom.searchresults

import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.BreadCrumbWidget
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.MainFramePage
import org.openqa.selenium.By

class SearchResultsPage : MainFramePage() {
    @Required val breadcrumb = BreadCrumbWidget(element("ul.uk-breadcrumb"))

    @Required val title = elements("h2.uk-article-title").first()

    val searchResults = WidgetsCollection(elements("article[id]"), ::SearchResultItemWidget)

    val pagination = SearchResultsPaginationWidget(element("nav[role=navigation]"))
}

class SearchResultItemWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("h2>a")
    val text = find("div.tm-article-content")
}

class SearchResultsPaginationWidget(self: SelenideElement) : Widget(self) {
    @Required val currentPage = find("ul.uk-pagination li.uk-active")
    @Required val pagesLinks = findAll("ul.uk-pagination li a")
    val nextPage = find(By.xpath(".//li/a[./span[contains(@class,'uk-icon-angle-double-right')]]"))
    val previousPage = find(By.xpath(".//li/a[./span[contains(@class,'uk-icon-angle-double-left')]]"))
}

val searchResultsPage = SearchResultsPage()
