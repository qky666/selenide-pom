package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class ToolsTabContentWidget(self: SelenideElement) : Widget(self) {
    @Required val tiddlywikiVersion = findX("./p")

    @Required val home = ToolItem(
        find("div[class*=home]"), mapOf(
            "en" to Condition.exactText("home"), "es" to Condition.exactText("Inicio")
        )
    )
}

class ToolItem(
    self: SelenideElement,
    buttonConditions: Map<String, Condition> = mapOf(),
    descriptionConditions: Map<String, Condition> = mapOf()
) : Widget(self) {
    @Required val checkbox = find("input")
    @Required val button = ConditionedElement(find("button"), buttonConditions)
    @Required val description = ConditionedElement(find("i"), descriptionConditions)
}
