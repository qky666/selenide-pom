@file:Suppress("unused")

package {{ cookiecutter.group }}.tiddlywikicucumber.cucumber.steps

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.CollectionCondition.sizeGreaterThan
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import io.cucumber.datatable.DataTable
import io.cucumber.java8.Es
import {{ cookiecutter.group }}.tiddlywikicucumber.pom.mainPage
import {{ cookiecutter.group }}.tiddlywikicucumber.pom.storyriver.GettingStartedTiddlerViewWidget
import org.apache.logging.log4j.kotlin.Logging
import org.openqa.selenium.By
import org.testng.Assert.assertEquals

class StepsDefinition : Es, Logging {

    init {
        Dado("Se accede a la web de TiddlyWiki en español") {
            Selenide.open(TestData.getString("project.baseUrl")!!)
            mainPage.shouldLoadRequired(lang = "spa").changeSiteLanguageIfNeeded()
            val firstTiddler = mainPage.storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
            GettingStartedTiddlerViewWidget(firstTiddler).shouldLoadRequired()
            mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(1))
        }

        Dado("Se pulsa en 'Abiertos: Cerrar todo'") {
            mainPage.showHideSidebar()
            mainPage.sidebar.sidebarTabs.openTabButton.click()
            mainPage.sidebar.sidebarTabs.openTabContent.shouldLoadRequired().closeAll.click(ClickOptions.usingJavaScript())
            mainPage.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
            mainPage.storyRiver.tiddlerViews.shouldHave(size(0))
            mainPage.storyRiver.tiddlerEdits.shouldHave(size(0))
        }

        Dado("Se verifica que el número de tiddlers abiertos en modo vista es {int}") { openTiddlers: Int ->
            mainPage.storyRiver.tiddlerViews.shouldHave(size(openTiddlers))
        }

        Dado("Se verifica que el número de tiddlers abiertos en modo edición es {int}") { openTiddlers: Int ->
            mainPage.storyRiver.tiddlerEdits.shouldHave(size(openTiddlers))
        }

        Dado("Se verifica que el número de tiddlers abiertos en modo edición es {int} y en modo vista es {int}") { openEdit: Int, openView: Int ->
            mainPage.storyRiver.tiddlerEdits.shouldHave(size(openEdit))
            mainPage.storyRiver.tiddlerViews.shouldHave(size(openView))
        }

        Dado("Se crea un nuevo tiddler") {
            val tiddlerEdits = mainPage.storyRiver.tiddlerEdits.size()
            mainPage.showHideSidebar()
            mainPage.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
            mainPage.storyRiver.tiddlerEdits.shouldHave(size(tiddlerEdits + 1))
        }

        Dado("Se recorren las pestañas del panel lateral") {
            mainPage.showHideSidebar()
            mainPage.sidebar.sidebarTabs.tabButtonToTabContentMap.forEach { (tabButton, tabContent) ->
                tabButton.click()
                tabContent.shouldLoadRequired()
            }
        }

        Dado("Se verifica que la pestaña Recientes tiene {int} elemento") { recent: Int ->
            mainPage.showHideSidebar()
            mainPage.sidebar.sidebarTabs.recentTabButton.click()
            mainPage.sidebar.sidebarTabs.recentTabContent.shouldLoadRequired().dateItems.shouldHave(size(recent))
        }

        Dado("Se informan los datos del primer tiddler abierto en modo edición") { table: DataTable ->
            val titleKey = "Título"
            val bodyKey = "Contenido"
            val maps = table.asMaps()
            assertEquals(maps.size, 1)
            val map = maps[0]
            val edits = mainPage.shouldLoadRequired().storyRiver.tiddlerEdits
            val editsSize = edits.size()
            val edit = mainPage.storyRiver.tiddlerEdits[0].shouldLoadRequired()
            map[titleKey]?.let {
                edit.titleInput.value = it
            }
            map[bodyKey]?.let {
                // Selenide helpers for shadow dom not working here (do not know why), so we do it the hard way with switchTo
                val webdriver = SPConfig.getWebDriver()!!
                webdriver.switchTo().frame(edit.bodyEditorIframe.wrappedElement)
                webdriver.findElement(By.cssSelector("textarea")).sendKeys(it)
                webdriver.switchTo().defaultContent()
            }
            edit.save.click()
            edits.shouldHave(size(editsSize - 1))
        }

        Dado("Se verifican los datos del primer tiddler abierto en modo vista") { table: DataTable ->
            val titleKey = "Título"
            val bodyKey = "Contenido"
            val maps = table.asMaps()
            assertEquals(maps.size, 1)
            val map = maps[0]
            val newTiddlerView = mainPage.shouldLoadRequired().storyRiver.tiddlerViews.shouldHave(sizeGreaterThan(0))[0]
            map[titleKey]?.let {
                newTiddlerView.title.shouldHave(exactText(it))
            }
            map[bodyKey]?.let {
                newTiddlerView.body.shouldHave(exactText(it))
            }
        }

        Dado("Se realiza una búsqueda por el término {string}, que obtiene {int} resultados y el texto {string}") { search: String, matches: Int, result: String ->
            mainPage.showHideSidebar()
            mainPage.sidebar.searchInput.value = search
            mainPage.searchPopup.shouldLoadRequired().matches.shouldHave(size(matches))
            mainPage.sidebar.searchResultsText.shouldHave(text(result))
        }

        Dado("Se pulsa sobre el primer resultado de la búsqueda") {
            mainPage.shouldLoadRequired()
            mainPage.searchPopup.shouldLoadRequired().matches.shouldHave(sizeGreaterThan(0))[0].click()
        }

        Dado("Se cierra el popup de búsqueda") {
            mainPage.shouldLoadRequired().sidebar.resetSearch.click()
            mainPage.searchPopup.should(disappear)
        }
    }
}
