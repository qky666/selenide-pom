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
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import io.cucumber.datatable.DataTable
import io.cucumber.java8.Es
import {{ cookiecutter.group }}.tiddlywikicucumber.pom.MainPage
import {{ cookiecutter.group }}.tiddlywikicucumber.pom.storyriver.GettingStartedTiddlerViewWidget
import org.apache.logging.log4j.kotlin.Logging
import org.openqa.selenium.By
import org.testng.Assert.assertEquals

class StepsDefinition : Es, Logging {

    init {
        Dado("Se accede a la web de TiddlyWiki en español") {
            Selenide.open(TestData.getString("project.baseUrl")!!)
            Page.getInstance(MainPage::class).let {
                it.shouldLoadRequired(lang = "spa").changeSiteLanguageIfNeeded()
                val firstTiddler = it.storyRiver.tiddlerViews.shouldHave(size(1))[0].shouldLoadRequired()
                GettingStartedTiddlerViewWidget(firstTiddler).shouldLoadRequired()
                it.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(1))
            }
        }

        Dado("Se pulsa en 'Abiertos: Cerrar todo'") {
            Page.getInstance(MainPage::class).let {
                it.showHideSidebar()
                it.sidebar.sidebarTabs.openTabButton.click()
                it.sidebar.sidebarTabs.openTabContent.shouldLoadRequired().closeAll.click(ClickOptions.usingJavaScript())
                it.sidebar.sidebarTabs.openTabContent.openItems.shouldHave(size(0))
                it.storyRiver.tiddlerViews.shouldHave(size(0))
                it.storyRiver.tiddlerEdits.shouldHave(size(0))
            }
        }

        Dado("Se verifica que el número de tiddlers abiertos en modo vista es {int}") { openTiddlers: Int ->
            Page.getInstance(MainPage::class).storyRiver.tiddlerViews.shouldHave(size(openTiddlers))
        }

        Dado("Se verifica que el número de tiddlers abiertos en modo edición es {int}") { openTiddlers: Int ->
            Page.getInstance(MainPage::class).storyRiver.tiddlerEdits.shouldHave(size(openTiddlers))
        }

        Dado("Se verifica que el número de tiddlers abiertos en modo edición es {int} y en modo vista es {int}") { openEdit: Int, openView: Int ->
            Page.getInstance(MainPage::class).let {
                it.storyRiver.tiddlerEdits.shouldHave(size(openEdit))
                it.storyRiver.tiddlerViews.shouldHave(size(openView))
            }
        }

        Dado("Se crea un nuevo tiddler") {
            Page.getInstance(MainPage::class).let {
                val tiddlerEdits = it.storyRiver.tiddlerEdits.size()
                it.showHideSidebar()
                it.sidebar.newTiddler.click(ClickOptions.usingJavaScript())
                it.storyRiver.tiddlerEdits.shouldHave(size(tiddlerEdits + 1))
            }
        }

        Dado("Se recorren las pestañas del panel lateral") {
            Page.getInstance(MainPage::class).let {
                it.showHideSidebar()
                it.sidebar.sidebarTabs.tabButtonToTabContentMap.forEach { (tabButton, tabContent) ->
                    tabButton.click()
                    tabContent.shouldLoadRequired()
                }
            }
        }

        Dado("Se verifica que la pestaña Recientes tiene {int} elemento") { recent: Int ->
            Page.getInstance(MainPage::class).let {
                it.showHideSidebar()
                it.sidebar.sidebarTabs.recentTabButton.click()
                it.sidebar.sidebarTabs.recentTabContent.shouldLoadRequired().dateItems.shouldHave(size(recent))
            }
        }

        Dado("Se informan los datos del primer tiddler abierto en modo edición") { table: DataTable ->
            val titleKey = "Título"
            val bodyKey = "Contenido"
            val maps = table.asMaps()
            assertEquals(maps.size, 1)
            val map = maps[0]
            Page.getInstance(MainPage::class).let {
                val edits = it.shouldLoadRequired().storyRiver.tiddlerEdits
                val editsSize = edits.size()
                val edit = it.storyRiver.tiddlerEdits[0].shouldLoadRequired()
                map[titleKey]?.let { title ->
                    edit.titleInput.value = title
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
        }

        Dado("Se verifican los datos del primer tiddler abierto en modo vista") { table: DataTable ->
            val titleKey = "Título"
            val bodyKey = "Contenido"
            val maps = table.asMaps()
            assertEquals(maps.size, 1)
            val map = maps[0]
            val newTiddlerView =
                Page.getInstance(MainPage::class).shouldLoadRequired().storyRiver.tiddlerViews.shouldHave(
                    sizeGreaterThan(0)
                )[0]
            map[titleKey]?.let { title ->
                newTiddlerView.title.shouldHave(exactText(title))
            }
            map[bodyKey]?.let { body ->
                newTiddlerView.body.shouldHave(exactText(body))
            }
        }

        Dado("Se realiza una búsqueda por el término {string}, que obtiene {int} resultados y el texto {string}") { search: String, matches: Int, result: String ->
            Page.getInstance(MainPage::class).let {
                it.showHideSidebar()
                it.sidebar.searchInput.value = search
                it.searchPopup.shouldLoadRequired().matches.shouldHave(size(matches))
                it.sidebar.searchResultsText.shouldHave(text(result))
            }
        }

        Dado("Se pulsa sobre el primer resultado de la búsqueda") {
            Page.getInstance(MainPage::class).shouldLoadRequired().searchPopup.shouldLoadRequired().matches.shouldHave(
                sizeGreaterThan(0)
            )[0].click()
        }

        Dado("Se cierra el popup de búsqueda") {
            Page.getInstance(MainPage::class).let {
                it.shouldLoadRequired().sidebar.resetSearch.click()
                it.searchPopup.should(disappear)
            }
        }
    }
}
