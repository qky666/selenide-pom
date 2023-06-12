package {{ cookiecutter.group }}.pom.searchresults

import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection
import {{ cookiecutter.group }}.pom.common.BreadCrumbWidget
import {{ cookiecutter.group }}.pom.common.MainFramePage
import java.time.Duration

class SearchResultsPage : MainFramePage() {
    @Required val breadcrumb = BreadCrumbWidget(find("ul.uk-breadcrumb"))

    // Possible bug in mtp.es, message is always displayed in spanish
    @Required val title = LangConditionedElement(find("h2.uk-article-title"), text("Resultados de búsqueda para:"))

    val searchResults = WidgetsCollection(findAll("article[id]"), ::SearchResultItemWidget)

    val pagination = SearchResultsPaginationWidget(find("nav[role=navigation]"))

    override fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
        super.customShouldLoadRequired(timeout, model, lang)
        breadcrumb.activeBreadcrumbItem.shouldHave(text("Results:"))
    }
}

class SearchResultItemWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("h2>a")
    val text = find("div.tm-article-content")
}

class SearchResultsPaginationWidget(self: SelenideElement) : Widget(self) {
    @Required val currentPage = find("ul.uk-pagination li.uk-active")

    @Required val pagesLinks = findAll("ul.uk-pagination li a")
    val nextPage = LangConditionedElement(
        findX(".//li/a[./span[contains(@class,'uk-icon-angle-double-right')]]"),
        mapOf(
            "es" to "Página siguiente",
            "en" to "Next Page"
        )
    )

    val previousPage = LangConditionedElement(
        findX(".//li/a[./span[contains(@class,'uk-icon-angle-double-left')]]"),
        mapOf(
            "es" to "Página anterior",
            "en" to "Previous Page"
        )
    )
}

val searchResultsPage = SearchResultsPage()
