package {{ cookiecutter.group }}.tiddlywikicucumber.pom.sidebar.tabs

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class MoreTabContentWidget(self: SelenideElement) : Widget(self) {
    @Required val selectedTabButton = find("div.tc-tab-buttons button.tc-tab-selected")

    @Required val allTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(1)"), mapOf("eng" to "All", "spa" to "Todos")
    )

    @Required val recentTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(2)"), mapOf("eng" to "Recent", "spa" to "Recientes")
    )

    @Required val tagsTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(3)"), mapOf("eng" to "Tags", "spa" to "Etiquetas")
    )

    @Required val missingTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(4)"), mapOf("eng" to "Missing", "spa" to "Vacíos")
    )

    @Required val draftsTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(5)"), mapOf("eng" to "Drafts", "spa" to "Borradores")
    )

    @Required val orphansTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(6)"), mapOf("eng" to "Orphans", "spa" to "Huérfanos")
    )

    @Required val typesTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(7)"), mapOf("eng" to "Types", "spa" to "Tipos")
    )

    @Required val systemTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(8)"), mapOf("eng" to "System", "spa" to "Sistema")
    )

    @Required val shadowsTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(9)"), mapOf("eng" to "Shadows", "spa" to "Ocultos")
    )

    @Required val explorerTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(10)"), mapOf("eng" to "Explorer", "spa" to "Explorar")
    )

    @Required val pluginsTabButton = LangConditionedElement(
        find("div.tc-tab-buttons button:nth-child(11)"), mapOf("eng" to "Plugins", "spa" to "Complementos")
    )
}
