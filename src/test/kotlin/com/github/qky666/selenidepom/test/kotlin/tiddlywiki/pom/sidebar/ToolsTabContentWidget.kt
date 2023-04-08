package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class ToolsTabContentWidget(self: SelenideElement) : Widget(self) {
    @Required val tiddlywikiVersion = findX("./p")

    @Required val home = ToolItem(
        find("div[class$=home]"), mapOf(
            "en" to Condition.exactText("home"), "es" to Condition.exactText("Inicio")
        ), mapOf(
            "en" to Condition.exactText("Open the default tiddlers"),
            "es" to Condition.exactText("Cierra todos los tiddlers abiertos y abre los que se muestran por defecto al inicio")
        )
    )

    @Required val closeAll = ToolItem(
        find("div[class$=close-all]"), mapOf(
            "en" to Condition.exactText("close all"), "es" to Condition.exactText("Cerrar todo")
        ), mapOf(
            "en" to Condition.exactText("Close all tiddlers"),
            "es" to Condition.exactText("Cierra todos los tiddlers")
        )
    )

    @Required val foldAll = ToolItem(
        find("div[class$=fold-all]"), mapOf(
            "en" to Condition.exactText("fold all tiddlers"), "es" to Condition.exactText("Comprimir todos")
        ), mapOf(
            "en" to Condition.exactText("Fold the bodies of all opened tiddlers"),
            "es" to Condition.exactText("Comprime la vista de todos los tiddlers abiertos")
        )
    )

    @Required val unfoldAll = ToolItem(
        find("div[class$=unfold-all]"), mapOf(
            "en" to Condition.exactText("unfold all tiddlers"), "es" to Condition.exactText("Desplegar todos")
        ), mapOf(
            "en" to Condition.exactText("Unfold the bodies of all opened tiddlers"),
            "es" to Condition.exactText("Despliega y muestra el contenido de todos los tiddlers abiertos")
        )
    )

    @Required val permaview = ToolItem(
        find("div[class$=permaview]"), mapOf(
            "en" to Condition.exactText("permaview"), "es" to Condition.exactText("Permaview")
        ), mapOf(
            "en" to Condition.exactText("Set browser address bar to a direct link to all the tiddlers in this story"),
            "es" to Condition.exactText("Crea en la barra de direcciones del navegador un enlace directo a todos los tiddlers abiertos")
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
