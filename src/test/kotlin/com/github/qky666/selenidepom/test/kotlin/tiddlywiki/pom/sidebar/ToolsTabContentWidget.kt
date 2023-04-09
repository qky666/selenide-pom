package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class ToolsTabContentWidget(self: SelenideElement) : Widget(self) {
    @Required val tiddlywikiVersion = findX("./p")

    @Required val home = ToolItem(
        find("div[class$=home]"), mapOf(
            "en" to "home", "es" to "Inicio"
        ), mapOf(
            "en" to "Open the default tiddlers",
            "es" to "Cierra todos los tiddlers abiertos y abre los que se muestran por defecto al inicio"
        )
    )

    @Required val closeAll = ToolItem(
        find("div[class$=close-all]"), mapOf(
            "en" to "close all", "es" to "Cerrar todo"
        ), mapOf(
            "en" to "Close all tiddlers", "es" to "Cierra todos los tiddlers"
        )
    )

    @Required val foldAll = ToolItem(
        find("div[class$=fold-all]"), mapOf(
            "en" to "fold all tiddlers", "es" to "Comprimir todos"
        ), mapOf(
            "en" to "Fold the bodies of all opened tiddlers", "es" to "Comprime la vista de todos los tiddlers abiertos"
        )
    )

    @Required val unfoldAll = ToolItem(
        find("div[class$=unfold-all]"), mapOf(
            "en" to "unfold all tiddlers", "es" to "Desplegar todos"
        ), mapOf(
            "en" to "Unfold the bodies of all opened tiddlers",
            "es" to "Despliega y muestra el contenido de todos los tiddlers abiertos"
        )
    )

    @Required val permaview = ToolItem(
        find("div[class$=permaview]"), mapOf(
            "en" to "permaview", "es" to "Permaview"
        ), mapOf(
            "en" to "Set browser address bar to a direct link to all the tiddlers in this story",
            "es" to "Crea en la barra de direcciones del navegador un enlace directo a todos los tiddlers abiertos"
        )
    )

    @Required val newTiddler = ToolItem(
        find("div[class$=new-tiddler]"), mapOf(
            "en" to "new tiddler", "es" to "Nuevo tiddler"
        ), mapOf(
            "en" to "Create a new tiddler", "es" to "Crea un tiddler nuevo"
        )
    )

    @Required val newJournal = ToolItem(
        find("div[class$=new-journal]"), mapOf(
            "en" to "new journal", "es" to "Nueva entrada"
        ), mapOf(
            "en" to "Create a new journal tiddler", "es" to "Crea una nueva entrada de diario"
        )
    )

    @Required val newImage = ToolItem(
        find("div[class$=new-image]"), mapOf(
            "en" to "new image", "es" to "Nueva imagen"
        ), mapOf(
            "en" to "Create a new image tiddler", "es" to "Crea un nuevo tiddler de imagen"
        )
    )

    @Required val import = ToolItem(
        find("div[class$=import]"), mapOf(
            "en" to "import", "es" to "Importar"
        ), mapOf(
            "en" to "Import many types of file including text, image, TiddlyWiki or JSON",
            "es" to "Importa multitud de tipos de archivo, incluyendo textos, imágenes, TiddlyWiki y JSON"
        )
    )

    @Required val exportAll = ToolItem(
        find("div[class$=export-page]"), mapOf(
            "en" to "export all", "es" to "Exportar todos"
        ), mapOf(
            "en" to "Export all tiddlers", "es" to "Exporta todos los tiddlers"
        )
    )

    @Required val controlPanel = ToolItem(
        find("div[class$=control-panel]"), mapOf(
            "en" to "control panel", "es" to "Panel de Control"
        ), mapOf(
            "en" to "Open control panel", "es" to "Abre el Panel de Control"
        )
    )

    @Required val advancedSearch = ToolItem(
        find("div[class$=advanced-search]"), mapOf(
            "en" to "advanced search", "es" to "Búsqueda avanzada"
        ), mapOf(
            "en" to "Advanced search", "es" to "Búsqueda avanzada"
        )
    )

    @Required val tiddlerManager = ToolItem(
        find("div[class$=manager]", 0), mapOf(
            "en" to "tiddler manager", "es" to "Administrador tiddler"
        ), mapOf(
            "en" to "Open tiddler manager", "es" to "Abre el administrador del tiddler"
        )
    )

    @Required val tagManager = ToolItem(
        find("div[class$=tag-manager]"), mapOf(
            "en" to "tag manager", "es" to "Administrador de etiquetas"
        ), mapOf(
            "en" to "Open tag manager", "es" to "Abre el gestor de etiquetas"
        )
    )

    @Required val language = ToolItem(
        find("div[class$=language]"), mapOf(
            "en" to "language", "es" to "Idioma"
        ), mapOf(
            "en" to "Choose the user interface language", "es" to "Selecciona idioma de la interfaz de usuario"
        )
    )

    val languageChooser = LanguageChooser(Page.find("tc-language-chooser"))
}

class ToolItem(
    self: SelenideElement,
    buttonConditions: Map<String, String> = mapOf(),
    descriptionConditions: Map<String, String> = mapOf()
) : Widget(self) {
    @Required val checkbox = find("input")
    @Required val button = ConditionedElement(find("button"), buttonConditions)
    @Required val description = ConditionedElement(find("i"), descriptionConditions)
}


class LanguageChooser(self: SelenideElement): Widget(self) {
    @Required val enGB = find("a[href$=en-GB]")
    @Required val esES = find("a[href$=es-ES]")
    @Required val chosen = find("div.tc-chosen a")
}
