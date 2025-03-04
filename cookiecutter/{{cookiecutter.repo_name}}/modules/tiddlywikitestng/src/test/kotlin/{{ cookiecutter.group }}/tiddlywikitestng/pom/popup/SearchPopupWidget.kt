package {{ cookiecutter.group }}.tiddlywikitestng.pom.popup

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class SearchPopupWidget(self: SelenideElement) : Widget(self) {
    @Required val titleMatchesLabel = LangConditionedElement(
        find("div.tc-search-results p:first-of-type small"), mapOf("eng" to "Title matches:", "spa" to "En título")
    )

    @Required val allMatchesLabel = LangConditionedElement(
        find("div.tc-search-results p:not(:first-of-type) small"),
        mapOf("eng" to "All matches:", "spa" to "Cualquier coincidencia")
    )

    val matches = findAll("div.tc-search-results p a")
}
