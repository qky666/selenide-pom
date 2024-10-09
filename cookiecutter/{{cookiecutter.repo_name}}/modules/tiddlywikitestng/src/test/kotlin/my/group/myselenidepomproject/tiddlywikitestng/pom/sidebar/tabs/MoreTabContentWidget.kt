package {{ cookiecutter.group }}.tiddlywikitestng.pom.sidebar.tabs

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class MoreTabContentWidget(self: SelenideElement) : Widget(self) {
    @Required val selectedTabButton = find("div.tc-tab-buttons button.tc-tab-selected")

    @Required val allTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(1)"), mapOf("en" to "All", "es" to "Todos")
    )

    @Required val recentTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(2)"), mapOf("en" to "Recent", "es" to "Recientes")
    )

    @Required val tagsTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(3)"), mapOf("en" to "Tags", "es" to "Etiquetas")
    )

    @Required val missingTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(4)"), mapOf("en" to "Missing", "es" to "Vacíos")
    )

    @Required val draftsTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(5)"), mapOf("en" to "Drafts", "es" to "Borradores")
    )

    @Required val orphansTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(6)"), mapOf("en" to "Orphans", "es" to "Huérfanos")
    )

    @Required val typesTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(7)"), mapOf("en" to "Types", "es" to "Tipos")
    )

    @Required val systemTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(8)"), mapOf("en" to "System", "es" to "Sistema")
    )

    @Required val shadowsTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(9)"), mapOf("en" to "Shadows", "es" to "Ocultos")
    )

    @Required val explorerTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(10)"), mapOf("en" to "Explorer", "es" to "Explorar")
    )

    @Required val pluginsTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(11)"), mapOf("en" to "Plugins", "es" to "Complementos")
    )
}
