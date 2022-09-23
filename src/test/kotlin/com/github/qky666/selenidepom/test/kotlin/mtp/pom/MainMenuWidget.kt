@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.annotation.Required
import com.github.qky666.selenidepom.pom.Widget

class MainMenuWidget(self: SelenideElement) : Widget(self) {
    @Required val services = this.find("li#servicios_menu")
    val servicesPopUp = this.find("div.dropdown-servicios")
    val servicesPopUpQualityAssurance = servicesPopUp.find("a[data-principal='Aseguramiento de la calidad']")
}
