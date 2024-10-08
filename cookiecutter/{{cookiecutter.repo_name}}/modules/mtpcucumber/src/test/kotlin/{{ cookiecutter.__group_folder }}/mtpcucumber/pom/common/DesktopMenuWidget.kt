package {{ cookiecutter.group }}.mtpcucumber.pom.common

import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class DesktopMenuWidget(self: SelenideElement) : Widget(self) {
    @Required val services = LangConditionedElement(
        find("li#servicios_menu>button"),
        mapOf("es" to "SERVICIOS", "en" to "SERVICES")
    )

    @Required val areas = LangConditionedElement(
        find("li#sectores_menu>button"),
        mapOf("es" to "SECTORES", "en" to "AREAS")
    )

    @Required val training = LangConditionedElement(
        find("li#formacion_menu>button"),
        mapOf("es" to "FORMACIÓN", "en" to "TRAINING")
    )

    @Required(lang = "es")
    val blog = LangConditionedElement(
        find("li#blog_menu>button"),
        mapOf("es" to exactText("BLOG")),
        false
    )

    @Required val talent = LangConditionedElement(
        find("li#talento_menu>button"),
        mapOf("es" to "TALENTO", "en" to "TALENT")
    )

    @Required val about = LangConditionedElement(
        find("li.contenedor>button.sobre_menu>a"),
        mapOf("es" to "SOBRE MTP", "en" to "ABOUT MTP")
    )

    @Required val contact = LangConditionedElement(
        find("li#contacto_menu>a"),
        mapOf("es" to "CONTACTO", "en" to "CONTACT US")
    )

    // Search
    val searchOpen = find("button#btn-menu")
    val searchMenu = SearchMenuWidget(find("form#search-menu"))

    // Services Menu
    val servicesPopUp = ServicesPopupMenuWidget(find("div.dropdown-servicios"))
}

class SearchMenuWidget(self: SelenideElement) : Widget(self) {
    @Required val searchInput = find("input[name=s]")
    @Required val doSearch = find("button.search-submit")
}

class ServicesPopupMenuWidget(self: SelenideElement) : Widget(self) {
    @Required val qualityAssurance = find("a[data-principal='Aseguramiento de la calidad']")
}
