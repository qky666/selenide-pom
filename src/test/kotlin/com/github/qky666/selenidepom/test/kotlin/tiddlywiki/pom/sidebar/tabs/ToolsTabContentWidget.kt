package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.sidebar.tabs

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.common.Required
import com.github.qky666.selenidepom.pom.web.LangConditionedElement
import com.github.qky666.selenidepom.pom.web.Page
import com.github.qky666.selenidepom.pom.web.Widget

class ToolsTabContentWidget(self: SelenideElement) : Widget(self) {
    @Required val tiddlywikiVersion = findX("./p")

    @Required val home = ToolItem(
        find("div[class$=home]"),
        mapOf(
            "en" to "home",
            "es" to "Inicio"
        ),
        mapOf(
            "en" to "Open the default tiddlers",
            "es" to "Cierra todos los tiddlers abiertos y abre los que se muestran por defecto al inicio"
        )
    )

    @Required val closeAll = ToolItem(
        find("div[class$=close-all]"),
        mapOf(
            "en" to "close all",
            "es" to "Cerrar todo"
        ),
        mapOf(
            "en" to "Close all tiddlers",
            "es" to "Cierra todos los tiddlers"
        )
    )

    @Required val foldAll = ToolItem(
        find("div[class$=fold-all]"),
        mapOf(
            "en" to "fold all tiddlers",
            "es" to "Comprimir todos"
        ),
        mapOf(
            "en" to "Fold the bodies of all opened tiddlers",
            "es" to "Comprime la vista de todos los tiddlers abiertos"
        )
    )

    @Required val unfoldAll = ToolItem(
        find("div[class$=unfold-all]"),
        mapOf(
            "en" to "unfold all tiddlers",
            "es" to "Desplegar todos"
        ),
        mapOf(
            "en" to "Unfold the bodies of all opened tiddlers",
            "es" to "Despliega y muestra el contenido de todos los tiddlers abiertos"
        )
    )

    @Required val permaview = ToolItem(
        find("div[class$=permaview]"),
        mapOf(
            "en" to "permaview",
            "es" to "Permaview"
        ),
        mapOf(
            "en" to "Set browser address bar to a direct link to all the tiddlers in this story",
            "es" to "Crea en la barra de direcciones del navegador un enlace directo a todos los tiddlers abiertos"
        )
    )

    @Required val newTiddler = ToolItem(
        find("div[class$=new-tiddler]"),
        mapOf(
            "en" to "new tiddler",
            "es" to "Nuevo tiddler"
        ),
        mapOf(
            "en" to "Create a new tiddler",
            "es" to "Crea un tiddler nuevo"
        )
    )

    @Required val newJournal = ToolItem(
        find("div[class$=new-journal]"),
        mapOf(
            "en" to "new journal",
            "es" to "Nueva entrada"
        ),
        mapOf(
            "en" to "Create a new journal tiddler",
            "es" to "Crea una nueva entrada de diario"
        )
    )

    @Required val newImage = ToolItem(
        find("div[class$=new-image]"),
        mapOf(
            "en" to "new image",
            "es" to "Nueva imagen"
        ),
        mapOf(
            "en" to "Create a new image tiddler",
            "es" to "Crea un nuevo tiddler de imagen"
        )
    )

    @Required val import = ToolItem(
        find("div[class$=import]"),
        mapOf(
            "en" to "import",
            "es" to "Importar"
        ),
        mapOf(
            "en" to "Import many types of file including text, image, TiddlyWiki or JSON",
            "es" to "Importa multitud de tipos de archivo, incluyendo textos, imágenes, TiddlyWiki y JSON"
        )
    )

    @Required val exportAll = ToolItem(
        find("div[class$=export-page]"),
        mapOf(
            "en" to "export all",
            "es" to "Exportar todos"
        ),
        mapOf(
            "en" to "Export all tiddlers",
            "es" to "Exporta todos los tiddlers"
        )
    )

    @Required val controlPanel = ToolItem(
        find("div[class$=control-panel]"),
        mapOf(
            "en" to "control panel",
            "es" to "Panel de Control"
        ),
        mapOf(
            "en" to "Open control panel",
            "es" to "Abre el Panel de Control"
        )
    )

    @Required val advancedSearch = ToolItem(
        find("div[class$=advanced-search]"),
        mapOf(
            "en" to "advanced search",
            "es" to "Búsqueda avanzada"
        ),
        mapOf(
            "en" to "Advanced search",
            "es" to "Búsqueda avanzada"
        )
    )

    @Required val tiddlerManager = ToolItem(
        find("div[class$=manager]", 0),
        mapOf(
            "en" to "tiddler manager",
            "es" to "Administrador tiddler"
        ),
        mapOf(
            "en" to "Open tiddler manager",
            "es" to "Abre el administrador del tiddler"
        )
    )

    @Required val tagManager = ToolItem(
        find("div[class$=tag-manager]"),
        mapOf(
            "en" to "tag manager",
            "es" to "Administrador de etiquetas"
        ),
        mapOf(
            "en" to "Open tag manager",
            "es" to "Abre el gestor de etiquetas"
        )
    )

    @Required val language = ToolItem(
        find("div[class$=language]"),
        mapOf(
            "en" to "language",
            "es" to "Idioma"
        ),
        mapOf(
            "en" to "Choose the user interface language",
            "es" to "Selecciona idioma de la interfaz de usuario"
        )
    )

    // val languageChooser = LanguageChooser(Page.find("div.tc-language-chooser"))
    val languageChooser = LanguageChooser(Page.findXAll(".//div[contains(@class,'tc-language-chooser')]")[0])

    @Required val palette = ToolItem(
        find("div[class$=palette]"),
        mapOf(
            "en" to "palette",
            "es" to "Paleta"
        ),
        mapOf(
            "en" to " Choose the colour palette",
            "es" to "Selecciona la paleta de color"
        )
    )

    @Required val theme = ToolItem(
        find("div[class$=theme]"),
        mapOf(
            "en" to "theme",
            "es" to "Tema"
        ),
        mapOf(
            "en" to "Choose the display theme",
            "es" to "Selecciona un estilo visual para el wiki"
        )
    )

    @Required val layout = ToolItem(
        find("div[class$=layout]"),
        mapOf(
            "en" to "layout",
            "es" to "disposición"
        ),
        mapOf(
            "en" to "Open the layout switcher",
            "es" to "Abre el selector de diseño"
        )
    )

    @Required val storyview = ToolItem(
        find("div[class$=storyview]"),
        mapOf(
            "en" to "storyview",
            "es" to "Vista"
        ),
        mapOf(
            "en" to "Choose the story visualisation",
            "es" to "Selecciona el modo de visualización de los tiddlers"
        )
    )

    @Required val setPassword = ToolItem(
        find("div[class$=encryption]"),
        mapOf(
            "en" to "set password",
            "es" to "Asignar contraseña"
        ),
        mapOf(
            "en" to "Set or clear a password for saving this wiki",
            "es" to "Asigna o revoca la contraseña de cifrado para este wiki"
        )
    )

    @Required val timestamp = ToolItem(
        find("div[class$=timestamp]"),
        mapOf(
            "en" to "timestamps are",
            "es" to "las marcas de tiempo están"
        ),
        mapOf(
            "en" to "Choose whether modifications update timestamps",
            "es" to "Elige si las modificaciones actualizan las marcas de tiempo"
        )
    )

    @Required val fullScreen = ToolItem(
        find("div[class$=full-screen]"),
        mapOf(
            "en" to "full-screen",
            "es" to "Pantalla completa"
        ),
        mapOf(
            "en" to "Enter or leave full-screen mode",
            "es" to "Entra y sale del modo de pantalla completa"
        )
    )

    @Required val printPage = ToolItem(
        find("div[class$=print]"),
        mapOf(
            "en" to "print page",
            "es" to "Imprimir página"
        ),
        mapOf(
            "en" to "Print the current page",
            "es" to "Imprime la página actual"
        )
    )

    @Required val saveChanges = ToolItem(
        find("div[class$=save-wiki]"),
        mapOf(
            "en" to "save changes",
            "es" to "Guardar cambios"
        ),
        mapOf(
            "en" to "Save changes",
            "es" to "Confirma y guarda todos los cambios realizados en el wiki"
        )
    )

    @Required val refresh = ToolItem(
        find("div[class$=refresh]"),
        mapOf(
            "en" to "refresh",
            "es" to "Recargar"
        ),
        mapOf(
            "en" to "Perform a full refresh of the wiki",
            "es" to "Actualiza completamente este wiki"
        )
    )

    @Required val more = ToolItem(
        find("div[class$=more-page-actions]"),
        mapOf(
            "en" to "more",
            "es" to "Más"
        ),
        mapOf(
            "en" to "More actions",
            "es" to "Otras acciones"
        )
    )
}

class ToolItem(
    self: SelenideElement,
    buttonConditions: Map<String, Condition> = mapOf(),
    descriptionConditions: Map<String, Condition> = mapOf(),
    strict: Boolean = true
) : Widget(self) {
    @Required val checkbox = find("input")

    @Required val button = LangConditionedElement(find("button"), buttonConditions, strict)

    @Required val description = LangConditionedElement(find("i"), descriptionConditions, strict)

    constructor(
        self: SelenideElement,
        buttonTexts: Map<String, String> = mapOf(),
        descriptionTexts: Map<String, String> = mapOf()
    ) : this(
        self,
        buttonTexts.mapValues { Condition.text(it.value) },
        descriptionTexts.mapValues { Condition.text(it.value) },
        true
    )
}

class LanguageChooser(self: SelenideElement) : Widget(self) {
    @Required val enGB = find("a[href$=en-GB]")

    @Required val esES = find("a[href$=es-ES]")

    @Required val chosen = find("div.tc-chosen a")
}
