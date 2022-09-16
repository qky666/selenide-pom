package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.annotation.Required
import com.github.qky666.selenidepom.pom.Page

open class MainFramePage : Page() {
    @Required("desktop") val mainMenu = MainMenuWidget(Selenide.element("div.custom-menu"))
    @Required("mobile") val mobileMenuButton = Selenide.element("button.custom-menu-btn-flotante")
    val mobileMenu = MobileMenuWidget(Selenide.element("div#menu-movil ul.uk-nav"))
    val cookiesBanner = CookiesBannerWidget(Selenide.element("div#cookie-law-info-bar"))
}

val mainFramePage = MainFramePage()
