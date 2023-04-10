package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar.tabs

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class MoreTabContentWidget(self: SelenideElement) : Widget(self) {
    @Required val selectedTabButton = find("div.tc-tab-buttons button.tc-tab-selected")

    @Required val allTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(1)"), mapOf("en" to "All", "es" to "Todos")
    )
    @Required val recentTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(2)"), mapOf("en" to "Recent", "es" to "Recientes")
    )
    @Required val tagsTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(3)"), mapOf("en" to "Tags", "es" to "Etiquetas")
    )
    @Required val missingTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(4)"), mapOf("en" to "Missing", "es" to "Vacíos")
    )
    @Required val draftsTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(5)"), mapOf("en" to "Drafts", "es" to "Borradores")
    )
    @Required val orphansTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(6)"), mapOf("en" to "Orphans", "es" to "Huérfanos")
    )
    @Required val typesTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(7)"), mapOf("en" to "Types", "es" to "Tipos")
    )
    @Required val systemTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(8)"), mapOf("en" to "System", "es" to "Sistema")
    )
    @Required val shadowsTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(9)"), mapOf("en" to "Shadows", "es" to "Ocultos")
    )
    @Required val explorerTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(10)"), mapOf("en" to "Explorer", "es" to "Explorar")
    )
    @Required val pluginsTabButton = ConditionedElement(
        find("div.tc-tab-buttons button:nth-child(11)"), mapOf("en" to "Plugins", "es" to "Complementos")
    )
}
