@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.annotation.Required
import com.github.qky666.selenidepom.pom.Widget

class MainMenuWidget(self: SelenideElement) : Widget(self) {
    @Required val home = this.find("a.img-menu")
    @Required val services = this.find("li#servicios_menu>a")
    @Required val sectors = this.find("li#sectores_menu>a")
    @Required val training = this.find("li#formacion_menu>a")
    @Required val blog = this.find("li#blog_menu>a")
    @Required val talent = this.find("li#talento_menu>a")
    @Required val about = this.find("li.sobre_menu>a")
    @Required val contact = this.find("li#contacto_menu>a")
    @Required val langEn = this.findAll("li.individual-menu-idioma>a").findBy(text("en"))
    @Required val langEs = this.findAll("li.individual-menu-idioma>a").findBy(text("es"))
    val openSearch = this.find("button#btn-menu")

    val servicesPopUp = this.find("div.dropdown-servicios")
    val servicesPopUpQualityAssurance = servicesPopUp.find("a[data-principal='Aseguramiento de la calidad']")
}
