package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.Selenide.element
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.menu.desktop.MainMenuWidget
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.menu.mobile.MobileMenuWidget

open class MainFramePage : Page() {
    @Required("desktop") val mainMenu = MainMenuWidget(element("div.custom-menu"))
    @Required("mobile") val mobileMenuButton = element("button.custom-menu-btn-flotante")
    val mobileMenu = MobileMenuWidget(element("div#menu-movil ul.uk-nav"))
    val cookiesBanner = CookiesBannerWidget(element("div#cookie-law-info-bar"))

    fun acceptCookies() {
        // This is what it should be, but for some reason, cookies message is not displayed when page loads
        // shouldLoadRequired().cookiesBanner.acceptCookies()

        // Workaround
        if (SPConfig.pomVersion == "mobile") {
            acceptCookiesMobile()
        } else {
            acceptCookiesDesktop()
        }
    }

    private fun acceptCookiesDesktop() {
        mainMenu.searchOpen.click()
        mainMenu.langEs.click(ClickOptions.withOffset(0, -50))
        cookiesBanner.acceptCookies()
        shouldLoadRequired()
    }

    private fun acceptCookiesMobile() {
        shouldLoadRequired().mobileMenuButton.click()
        cookiesBanner.acceptCookies()
    }
}

val mainFramePage = MainFramePage()
