package com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.home

import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.MainFramePage

class HomePage : MainFramePage() {
    @Required val mainBanner = MainBannerWidget(element("div.custom-bg-primary"))
}

class MainBannerWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("h1")
    @Required val text = find("p")
    @Required val moreInfo = find("a")

    fun verifyTextsEs() {
        title.shouldHave(exactText("MTP, 25 años como referencia en aseguramiento de negocios digitales"))
        text.shouldHave(exactText("Acompañamos a nuestros clientes en su transformación digital y asegurando la calidad de software, experiencia de usuario, seguridad y los desarrollos durante todo el ciclo de vida."))
        moreInfo.shouldHave(exactText("Más información"))
    }
}

val homePage = HomePage()
