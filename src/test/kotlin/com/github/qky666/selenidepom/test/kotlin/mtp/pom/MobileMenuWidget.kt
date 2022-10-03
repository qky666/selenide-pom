@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.CollectionCondition.allMatch
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.annotation.Required
import com.github.qky666.selenidepom.pom.Widget
import org.openqa.selenium.WebElement


class MobileMenuWidget(self: SelenideElement) : Widget(self) {
    // First level menu items
    @Required val services = this.findAll("li[aria-expanded]>a").findBy(text("Servicios"))
    @Required val sectors = this.findAll("li[aria-expanded]>a").findBy(text("Sectores"))
    @Required val training = this.findAll("li[aria-expanded]>a").findBy(text("Formación"))
    @Required val blog = this.findAll("li[aria-expanded]>a").findBy(text("Blog"))
    @Required val talent = this.findAll("li[aria-expanded]>a").findBy(text("Talento"))
    @Required val about = this.findAll("li[aria-expanded]>a").findBy(text("Sobre MTP"))
    @Required val contact = this.findAll("li>a").findBy(text("Contacto"))

    // All first level menu items
    val firstLevelMenuItems = this.findAll("li.uk-parent")

    // Second level menú items. I only write one, but there are more
    val servicesQualityAssurance = this.findAll("a").findBy(text("Aseguramiento de la calidad"))

    fun shouldBeCollapsed() {
        firstLevelMenuItems.shouldHave(allMatch("All firstLevelMenuItems have aria-expanded=false") { element: WebElement ->
            "false".equals(element.getAttribute("aria-expanded"), ignoreCase = true)
        })
    }
}
