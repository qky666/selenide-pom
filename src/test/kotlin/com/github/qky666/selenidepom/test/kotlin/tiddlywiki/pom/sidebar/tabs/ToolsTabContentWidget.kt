package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar.tabs

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class ToolsTabContentWidget(self: SelenideElement) : Widget(self) {
    @Required val tiddlywikiVersion = findX("./p")

    @Required val home = ToolItem(
        find("div[data-title$=home]"), mapOf("eng" to "home", "spa" to "Inicio"), mapOf(
            "eng" to "Open the default tiddlers",
            "spa" to "Cierra todos los tiddlers abiertos y abre los que se muestran por defecto al inicio"
        )
    )

    @Required val closeAll = ToolItem(
        find("div[data-title$=close-all]"), mapOf("eng" to "close all", "spa" to "Cerrar todo"), mapOf(
            "eng" to "Close all tiddlers", "spa" to "Cierra todos los tiddlers"
        )
    )

    @Required val foldAll = ToolItem(
        find("div[data-title$=fold-all]"), mapOf("eng" to "fold all tiddlers", "spa" to "Comprimir todos"), mapOf(
            "eng" to "Fold the bodies of all opened tiddlers",
            "spa" to "Comprime la vista de todos los tiddlers abiertos"
        )
    )

    @Required val unfoldAll = ToolItem(
        find("div[data-title$=unfold-all]"), mapOf("eng" to "unfold all tiddlers", "spa" to "Desplegar todos"), mapOf(
            "eng" to "Unfold the bodies of all opened tiddlers",
            "spa" to "Despliega y muestra el contenido de todos los tiddlers abiertos"
        )
    )

    @Required val permaview = ToolItem(
        find("div[data-title$=permaview]"), mapOf("eng" to "permaview", "spa" to "Permaview"), mapOf(
            "eng" to "Set browser address bar to a direct link to all the tiddlers in this story",
            "spa" to "Crea en la barra de direcciones del navegador un enlace directo a todos los tiddlers abiertos"
        )
    )

    @Required val newTiddler = ToolItem(
        find("div[data-title$=new-tiddler]"),
        mapOf("eng" to "new tiddler", "spa" to "Nuevo tiddler"),
        mapOf("eng" to "Create a new tiddler", "spa" to "Crea un tiddler nuevo")
    )

    @Required val newJournal = ToolItem(
        find("div[data-title$=new-journal]"),
        mapOf("eng" to "new journal", "spa" to "Nueva entrada"),
        mapOf("eng" to "Create a new journal tiddler", "spa" to "Crea una nueva entrada de diario")
    )

    @Required val newImage = ToolItem(
        find("div[data-title$=new-image]"),
        mapOf("eng" to "new image", "spa" to "Nueva imagen"),
        mapOf("eng" to "Create a new image tiddler", "spa" to "Crea un nuevo tiddler de imagen")
    )

    @Required val import = ToolItem(
        find("div[data-title$=import]"), mapOf("eng" to "import", "spa" to "Importar"), mapOf(
            "eng" to "Import many types of file including text, image, TiddlyWiki or JSON",
            "spa" to "Importa multitud de tipos de archivo, incluyendo textos, imágenes, TiddlyWiki y JSON"
        )
    )

    @Required val exportAll = ToolItem(
        find("div[data-title$=export-page]"),
        mapOf("eng" to "export all", "spa" to "Exportar todos"),
        mapOf("eng" to "Export all tiddlers", "spa" to "Exporta todos los tiddlers")
    )

    @Required val controlPanel = ToolItem(
        find("div[data-title$=control-panel]"),
        mapOf("eng" to "control panel", "spa" to "Panel de Control"),
        mapOf("eng" to "Open control panel", "spa" to "Abre el Panel de Control")
    )

    @Required val advancedSearch = ToolItem(
        find("div[data-title$=advanced-search]"),
        mapOf("eng" to "advanced search", "spa" to "Búsqueda avanzada"),
        mapOf("eng" to "Advanced search", "spa" to "Búsqueda avanzada")
    )

    @Required val tiddlerManager = ToolItem(
        find("div[data-title$=manager]", 0),
        mapOf("eng" to "tiddler manager", "spa" to "Administrador tiddler"),
        mapOf("eng" to "Open tiddler manager", "spa" to "Abre el administrador del tiddler")
    )

    @Required val tagManager = ToolItem(
        find("div[data-title$=tag-manager]"),
        mapOf("eng" to "tag manager", "spa" to "Administrador de etiquetas"),
        mapOf("eng" to "Open tag manager", "spa" to "Abre el gestor de etiquetas")
    )

    @Required val language = ToolItem(
        find("div[data-title$=language]"),
        mapOf("eng" to "language", "spa" to "Idioma"),
        mapOf("eng" to "Choose the user interface language", "spa" to "Selecciona idioma de la interfaz de usuario")
    )

    // val languageChooser = LanguageChooser(Page.find("div.tc-language-chooser"))
    val languageChooser = LanguageChooser(Page.findXAll(".//div[contains(@class,'tc-language-chooser')]")[0])

    @Required val palette = ToolItem(
        find("div[data-title$=palette]"),
        mapOf("eng" to "palette", "spa" to "Paleta"),
        mapOf("eng" to " Choose the colour palette", "spa" to "Selecciona la paleta de color")
    )

    @Required val theme = ToolItem(
        find("div[data-title$=theme]"),
        mapOf("eng" to "theme", "spa" to "Tema"),
        mapOf("eng" to "Choose the display theme", "spa" to "Selecciona un estilo visual para el wiki")
    )

    @Required val layout = ToolItem(
        find("div[data-title$=layout]"),
        mapOf("eng" to "layout", "spa" to "disposición"),
        mapOf("eng" to "Open the layout switcher", "spa" to "Abre el selector de diseño")
    )

    @Required val storyview = ToolItem(
        find("div[data-title$=storyview]"),
        mapOf("eng" to "storyview", "spa" to "Vista"),
        mapOf("eng" to "Choose the story visualisation", "spa" to "Selecciona el modo de visualización de los tiddlers")
    )

    @Required val setPassword = ToolItem(
        find("div[data-title$=encryption]"), mapOf("eng" to "set password", "spa" to "Asignar contraseña"), mapOf(
            "eng" to "Set or clear a password for saving this wiki",
            "spa" to "Asigna o revoca la contraseña de cifrado para este wiki"
        )
    )

    @Required val timestamp = ToolItem(
        find("div[data-title$=timestamp]"),
        mapOf("eng" to "timestamps are", "spa" to "las marcas de tiempo están"),
        mapOf(
            "eng" to "Choose whether modifications update timestamps",
            "spa" to "Elige si las modificaciones actualizan las marcas de tiempo"
        )
    )

    @Required val fullScreen = ToolItem(
        find("div[data-title$=full-screen]"),
        mapOf("eng" to "full-screen", "spa" to "Pantalla completa"),
        mapOf("eng" to "Enter or leave full-screen mode", "spa" to "Entra y sale del modo de pantalla completa")
    )

    @Required val printPage = ToolItem(
        find("div[data-title$=print]"),
        mapOf("eng" to "print page", "spa" to "Imprimir página"),
        mapOf("eng" to "Print the current page", "spa" to "Imprime la página actual")
    )

    @Required val saveChanges = ToolItem(
        find("div[data-title$=save-wiki]"),
        mapOf("eng" to "save changes", "spa" to "Guardar cambios"),
        mapOf("eng" to "Save changes", "spa" to "Confirma y guarda todos los cambios realizados en el wiki")
    )

    @Required val refresh = ToolItem(
        find("div[data-title$=refresh]"),
        mapOf("eng" to "refresh", "spa" to "Recargar"),
        mapOf("eng" to "Perform a full refresh of the wiki", "spa" to "Actualiza completamente este wiki")
    )

    @Required val more = ToolItem(
        find("div[data-title$=more-page-actions]"),
        mapOf("eng" to "more", "spa" to "Más"),
        mapOf("eng" to "More actions", "spa" to "Otras acciones")
    )
}

class ToolItem(
    self: SelenideElement,
    buttonConditions: Map<String, WebElementCondition> = mapOf(),
    descriptionConditions: Map<String, WebElementCondition> = mapOf(),
    strict: Boolean = true,
) : Widget(self) {
    @Required val checkbox = find("input")
    @Required val button = LangConditionedElement(find("button"), buttonConditions, strict)
    @Required val description = LangConditionedElement(find("i"), descriptionConditions, strict)

    @Suppress("RedundantValueArgument")
    constructor(
        self: SelenideElement,
        buttonTexts: Map<String, String> = mapOf(),
        descriptionTexts: Map<String, String> = mapOf(),
    ) : this(
        self,
        buttonTexts.mapValues { Condition.text(it.value) },
        descriptionTexts.mapValues { Condition.text(it.value) },
        true
    )
}

class LanguageChooser(self: SelenideElement) : Widget(self) {
    @Required val enGB = LangConditionedElement(find("a[href$=en-GB]"), "English (British)")
    @Required val esES = LangConditionedElement(find("a[href$=es-ES]"), "Castellano. (España)")
    @Required val chosen = find("div.tc-chosen a")
}
