package com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.home

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.frame.MainFramePage

class HomePage : MainFramePage() {
    @Required val mainBanner = MainBannerWidget(element("div.custom-bg-primary"))

    fun open() {
        Selenide.open(TestData.input.getProperty("data.input.baseUrl"))
        shouldLoadRequired(lang = "es")
    }
}

class MainBannerWidget(self: SelenideElement) : Widget(self) {
    @Required val title = ConditionedElement(
        find("h1"),
        mapOf(
            "es" to Condition.exactText("MTP, referencia en aseguramiento de negocios digitales"),
            "en" to Condition.exactText("MTP ensures quality digital public services")
        )
    )

    @Required val text = ConditionedElement(
        find("p"),
        mapOf(
            "es" to Condition.exactText("Acompañamos a nuestros clientes en su transformación digital y asegurando la calidad de software, experiencia de usuario, seguridad y los desarrollos durante todo el ciclo de vida."),
            "en" to Condition.exactText("MTP, the drive for the digital transformation of public administrations")
        )
    )

    @Required val moreInfo = ConditionedElement(
        find("a"),
        mapOf(
            "es" to Condition.text("Más información"),
            "en" to Condition.text("More information")
        )
    )
}

val homePage = HomePage()
