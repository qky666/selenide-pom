@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.qky666.selenidepom.test.kotlin.mtp.pom.menu.mobile

import com.codeborne.selenide.CollectionCondition.allMatch
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import org.openqa.selenium.WebElement


class MobileMenuWidget(self: SelenideElement) : Widget(self) {
    // First level menu items
    @Required val services = findAll("li[aria-expanded]>a").findBy(text("Servicios"))
    @Required val sectors = findAll("li[aria-expanded]>a").findBy(text("Sectores"))
    @Required val training = findAll("li[aria-expanded]>a").findBy(text("Formación"))
    @Required val blog = findAll("li[aria-expanded]>a").findBy(text("Blog"))
    @Required val talent = findAll("li[aria-expanded]>a").findBy(text("Talento"))
    @Required val about = findAll("li[aria-expanded]>a").findBy(text("Sobre MTP"))
    @Required val contact = findAll("li>a").findBy(text("Contacto"))

    // All first level menu items
    val firstLevelMenuItems = findAll("li.uk-parent")

    // Second level menú items. I only write one, but there are more
    val servicesQualityAssurance = findAll("a").findBy(text("Aseguramiento de la calidad"))

    fun shouldBeCollapsed() {
        firstLevelMenuItems.shouldHave(allMatch("All firstLevelMenuItems have aria-expanded=false") { element: WebElement ->
            "false".equals(element.getAttribute("aria-expanded"), ignoreCase = true)
        })
    }
}
