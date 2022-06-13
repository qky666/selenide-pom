package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.Required
import com.github.qky666.selenidepom.Page

open class MainFramePage : Page() {
    @Required("desktop") val mainMenu = MainMenuWidget()
    @Required("mobile") val mobileMenuButton = Selenide.element("button.custom-menu-btn-flotante")
    val mobileMenu = MobileMenuWidget()
    val cookiesBanner = CookiesBannerWidget()
}

val mainFramePage = MainFramePage()
