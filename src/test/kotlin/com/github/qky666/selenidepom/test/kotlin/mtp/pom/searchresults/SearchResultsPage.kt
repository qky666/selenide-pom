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
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.MainFramePage
import org.openqa.selenium.By
import java.time.Duration

class SearchResultsPage : MainFramePage() {
    @Required val breadcrumb = BreadCrumbWidget(element("ul.uk-breadcrumb"))

    // Possible bug in mtp.es, message is always displayed in spanish
    @Required val title =
        ConditionedElement(elements("h2.uk-article-title").first(), Condition.text("Resultados de búsqueda para:"))

    val searchResults = WidgetsCollection(elements("article[id]"), ::SearchResultItemWidget)

    val pagination = SearchResultsPaginationWidget(element("nav[role=navigation]"))

    override fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
        super.customShouldLoadRequired(timeout, model, lang)
        breadcrumb.activeBreadcrumbItem.shouldHave(Condition.text("Results:"))
    }
}

class SearchResultItemWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("h2>a")
    val text = find("div.tm-article-content")
}

class SearchResultsPaginationWidget(self: SelenideElement) : Widget(self) {
    @Required val currentPage = find("ul.uk-pagination li.uk-active")
    @Required val pagesLinks = findAll("ul.uk-pagination li a")
    val nextPage = ConditionedElement(
        find(By.xpath(".//li/a[./span[contains(@class,'uk-icon-angle-double-right')]]")),
        mapOf(
            "es" to Condition.text("Página siguiente"),
            "en" to Condition.text("Next Page"),
        )
    )
    val previousPage = ConditionedElement(
        find(By.xpath(".//li/a[./span[contains(@class,'uk-icon-angle-double-left')]]")),
        mapOf(
            "es" to Condition.text("Página anterior"),
            "en" to Condition.text("Previous Page"),
        )
    )
}

val searchResultsPage = SearchResultsPage()
