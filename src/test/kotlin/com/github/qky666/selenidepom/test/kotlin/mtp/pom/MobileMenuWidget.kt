@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.CollectionCondition.allMatch
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.annotation.Required
import com.github.qky666.selenidepom.pom.Widget
import org.openqa.selenium.WebElement


class MobileMenuWidget(self: SelenideElement) : Widget(self) {
    // First level menu items. I only write one, but there are more
    @Required val services = this.find(Selectors.byXpath(".//li[@aria-expanded]/a[.='Servicios']"))

    // All first level menu items
    val firstLevelMenuItems = this.findAll("li.uk-parent")

    // Second level menú items. I only write one, but there are more
    val servicesQualityAssurance = this.find(Selectors.byXpath(".//a[.='Aseguramiento de la calidad']"))

    fun shouldBeCollapsed() {
        firstLevelMenuItems.shouldHave(allMatch("All firstLevelMenuItems have aria-expanded=false") { element: WebElement ->
            "false".equals(element.getAttribute("aria-expanded"), ignoreCase = true)
        })
    }
}
