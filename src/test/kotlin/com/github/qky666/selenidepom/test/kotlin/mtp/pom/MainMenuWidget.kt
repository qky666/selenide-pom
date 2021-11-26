@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.kotlin.Required
import com.github.qky666.selenidepom.kotlin.Widget

class MainMenuWidget(override val self: SelenideElement = Selenide.element("div.custom-menu")) : Widget(self) {
    @Required val services = self.find("li#servicios_menu")
    val servicesPopUp = self.find("div.dropdown-servicios")
    val servicesPopUpQualityAssurance = servicesPopUp.find("a[data-principal='Aseguramiento de la calidad']")
}
