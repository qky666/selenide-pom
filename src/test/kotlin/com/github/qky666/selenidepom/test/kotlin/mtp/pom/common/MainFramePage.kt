package com.github.qky666.selenidepom.test.kotlin.mtp.pom.common

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.Selenide.element
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.shouldLoadRequired

open class MainFramePage : Page() {
    @Required val home = element("a.img-menu")
    @Required(model = "desktop") val desktopMenu = DesktopMenuWidget(element("nav.menu-pc"))
    @Required(model = "mobile") val mobileMenu = MobileMenuWidget(element("div.menu-movil"))
    val mobileMenuPopUp = MobileMenuPopUpWidget(element("div#menu-movil"))
    val cookiesBanner = CookiesBannerWidget(element("div#cookie-law-info-bar"))

    fun acceptCookies() {
        // This is what it should be, but for some reason, cookies message is not displayed when page loads
        // shouldLoadRequired().cookiesBanner.acceptCookies()

        // Workaround
        if (SPConfig.model == "mobile") {
            acceptCookiesMobile()
        } else {
            acceptCookiesDesktop()
        }
    }

    private fun acceptCookiesDesktop() {
        desktopMenu.searchOpen.click()
        desktopMenu.langEs.click(ClickOptions.withOffset(0, -50))
        cookiesBanner.acceptCookies()
        shouldLoadRequired()
    }

    private fun acceptCookiesMobile() {
        shouldLoadRequired().mobileMenu.mobileMenuButton.click()
        cookiesBanner.acceptCookies()
        shouldLoadRequired()
    }
}

@Suppress("unused") val mainFramePage = MainFramePage()
