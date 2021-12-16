@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Selectors
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.Required
import com.github.qky666.selenidepom.Widget

class MobileMenuWidget(override val self: SelenideElement = Selenide.element("div#menu-movil ul.uk-nav")) : Widget(self) {
    @Required val services = self.find(Selectors.byXpath(".//li[@aria-expanded]/a[.='Servicios']"))
    val servicesQualityAssurance = self.find(Selectors.byXpath(".//a[.='Aseguramiento de la calidad']"))
}
