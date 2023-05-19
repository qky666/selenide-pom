package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.popup

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Widget

class SearchPopupWidget(self: SelenideElement) : Widget(self) {
    @Required val titleMatchesLabel = LangConditionedElement(
        find("div.tc-search-results p:first-of-type small"),
        mapOf(
            "en" to "Title matches:",
            "es" to "En título"
        )
    )

    @Required val allMatchesLabel = LangConditionedElement(
        find("div.tc-search-results p:not(:first-of-type) small"),
        mapOf(
            "en" to "All matches:",
            "es" to "Cualquier coincidencia"
        )
    )

    val matches = findAll("div.tc-search-results p a")
}
