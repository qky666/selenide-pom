@file:Suppress("unused")

package {{ cookiecutter.group }}.cucumber.steps

import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import io.cucumber.java8.Es
import org.apache.logging.log4j.kotlin.Logging
import org.testng.Assert
import {{ cookiecutter.group }}.pom.common.mainFramePage
import {{ cookiecutter.group }}.pom.pages.home.homePage
import {{ cookiecutter.group }}.pom.pages.services.qualityAssurancePage
import {{ cookiecutter.group }}.pom.pages.services.servicesPage
import {{ cookiecutter.group }}.pom.searchresults.searchResultsPage

class MtpStepsDefinition : Es, Logging {

    init {
        Dado("Se accede a la web de MTP") {
            homePage.open()
        }

        Dado("Se establece el idioma") {
            homePage.setLangIfNeeded()
        }

        Dado("Se aceptan las cookies") {
            mainFramePage.acceptCookies()
        }

        Cuando("Se navega a Servicios -> Aseguramiento de la calidad") {
            when (SPConfig.model) {
                "mobile" -> {
                    mainFramePage.shouldLoadRequired().mobileMenu.mobileMenuButton.click()
                    val mobileMenu = mainFramePage.mobileMenuPopUp
                    mobileMenu.shouldLoadRequired().shouldBeCollapsed()
                    mobileMenu.services().click()
                    mobileMenu.servicesQualityAssurance().shouldBe(visible).click()
                }
                "desktop" -> {
                    mainFramePage.shouldLoadRequired().desktopMenu.services.hover()
                    mainFramePage.desktopMenu.servicesPopUp.qualityAssurance.click()
                }
                else -> throw RuntimeException("Model ${SPConfig.model} not found")
            }
            qualityAssurancePage.shouldLoadRequired()
        }

        Entonces("Se carga la página Aseguramiento de la calidad") {
            servicesPage.shouldLoadRequired()
        }

        Entonces("El mensaje de aviso de las cookies no se muestra") {
            mainFramePage.shouldLoadRequired().cookiesBanner.shouldNotBe(visible)
        }

        Cuando("Se busca el término '{}'") { search: String ->
            mainFramePage.shouldLoadRequired().desktopMenu.searchOpen.click()
            mainFramePage.desktopMenu.searchMenu.shouldLoadRequired().searchInput.sendKeys(search)
            mainFramePage.desktopMenu.searchMenu.doSearch.click()
            mainFramePage.desktopMenu.searchMenu.should(disappear)
        }

        Entonces("El número de páginas de resultados para la búsqueda '{}' es {int}") { search: String, resultsPages: Int ->
            val maxResultsPerPage = 5
            searchResultsPage.shouldLoadRequired().breadcrumb.activeBreadcrumbItem.shouldHave(exactText("Results: $search"))
            searchResultsPage.breadcrumb.breadcrumbItems[0].shouldHave(exactText("Home"))
            Assert.assertEquals(searchResultsPage.searchResults.shouldLoadRequired().count(), maxResultsPerPage)

            if (resultsPages > 1) {
                searchResultsPage.pagination.shouldLoadRequired().currentPage.shouldHave(exactText("1"))
                searchResultsPage.pagination.nextPage.shouldBe(visible)
                searchResultsPage.pagination.pagesLinks.shouldHave(size(resultsPages))[resultsPages - 2].shouldHave(
                    exactText(resultsPages.toString())
                )
            } else {
                searchResultsPage.pagination.shouldNotBe(visible)
            }
        }

        Cuando("Se navega a la página {int} de {int} de resultados de la búsqueda") { page: Int, totalPages: Int ->
            if (totalPages == 1 && page == 1) {
                searchResultsPage.shouldLoadRequired().pagination.shouldNotBe(visible)
            } else {
                searchResultsPage.shouldLoadRequired().pagination.shouldLoadRequired().pagesLinks.find(exactText(page.toString()))
                    .click()
                searchResultsPage.shouldLoadRequired().pagination.shouldLoadRequired().currentPage.shouldHave(
                    exactText(page.toString())
                )
            }
        }

        Entonces("La página mostrada es la última del total de {int} páginas de resultados de la búsqueda") { totalPages: Int ->
            if (totalPages == 1) {
                searchResultsPage.shouldLoadRequired().pagination.shouldNotBe(visible)
            } else {
                searchResultsPage.shouldLoadRequired().pagination.nextPage.should(disappear)
                searchResultsPage.pagination.previousPage.shouldBe(visible)
            }
        }

        Entonces("El número de resultados para la búsqueda mostrados es {int}") { results: Int ->
            Assert.assertEquals(
                searchResultsPage.shouldLoadRequired().searchResults.shouldLoadRequired().count(),
                results
            )
        }

        Entonces("Se muestra un resultado para la búsqueda con título '{}' y texto '{}'") { search: String, textBody: String ->
            val result =
                searchResultsPage.shouldLoadRequired().searchResults.filterBy(text(search)).shouldHave(size(1))[0]
            result.title.shouldHave(exactText(search))

            if (textBody.isNotEmpty()) {
                result.text.shouldHave(text(textBody))
            } else {
                result.text.shouldNotBe(visible)
            }
        }
    }
}
