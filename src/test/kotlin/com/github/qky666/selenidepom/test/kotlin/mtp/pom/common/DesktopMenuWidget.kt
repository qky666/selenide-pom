package com.github.qky666.selenidepom.test.kotlin.mtp.pom.common

import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class DesktopMenuWidget(self: SelenideElement) : Widget(self) {
    @Required val services = ConditionedElement(
        find("li#servicios_menu>a"),
        mapOf("es" to exactText("SERVICIOS"), "en" to exactText("SERVICES")),
    )
    @Required val areas = ConditionedElement(
        find("li#sectores_menu>a"),
        mapOf("es" to exactText("SECTORES"), "en" to exactText("AREAS")),
    )
    @Required val training = ConditionedElement(
        find("li#formacion_menu>a"),
        mapOf("es" to exactText("FORMACIÓN"), "en" to exactText("TRAINING")),
    )
    @Required(lang = "es") val blog = ConditionedElement(
        find("li#blog_menu>a"),
        mapOf("es" to exactText("BLOG")),
        false,
    )
    @Required val talent = ConditionedElement(
        find("li#talento_menu>a"),
        mapOf("es" to exactText("TALENTO"), "en" to exactText("TALENT")),
    )
    @Required val about = ConditionedElement(
        find("li.sobre_menu>a"),
        mapOf("es" to exactText("SOBRE MTP"), "en" to exactText("ABOUT MTP")),
    )
    @Required val contact = ConditionedElement(
        find("li#contacto_menu>a"),
        mapOf("es" to exactText("CONTACTO"), "en" to exactText("CONTACT US")),
    )
    @Required val langEn = findAll("li.individual-menu-idioma>a").findBy(text("en"))
    @Required val langEs = findAll("li.individual-menu-idioma>a").findBy(text("es"))

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