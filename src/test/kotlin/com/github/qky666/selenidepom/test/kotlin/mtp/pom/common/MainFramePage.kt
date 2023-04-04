package com.github.qky666.selenidepom.test.kotlin.mtp.pom.common

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.Selenide.element
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.hasLoadedRequired
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import mu.KotlinLogging

open class MainFramePage : Page() {
    @Required val home = element("a.img-menu")

    @Required(model = "desktop")
    val desktopMenu = DesktopMenuWidget(element("nav.menu-pc"))

    @Required(model = "mobile")
    val mobileMenu = MobileMenuWidget(element("div.menu-movil"))
    val mobileMenuPopUp = MobileMenuPopUpWidget(element("div#menu-movil"))
    val cookiesBanner = CookiesBannerWidget(element("div#cookie-law-info-bar"))

    private val logger = KotlinLogging.logger {}

    fun acceptCookies() {
        // This is what it should be, but for some reason, cookies message is not displayed when page loads
        // shouldLoadRequired().cookiesBanner.acceptCookies()

        // Workaround
        if (SPConfig.model == "mobile") {
            acceptCookiesMobile()
        } else {
            acceptCookiesDesktop()
        }
        logger.info { "Cookies accepted" }
    }

    private fun acceptCookiesDesktop() {
        do {
            desktopMenu.searchOpen.click()
            desktopMenu.langEs.click(ClickOptions.withOffset(0, -50))
        } while (!cookiesBanner.hasLoadedRequired())
        cookiesBanner.acceptCookies()
        shouldLoadRequired()
    }

    private fun acceptCookiesMobile() {
        shouldLoadRequired()
        do {
            mobileMenu.mobileMenuButton.click()
        } while (!cookiesBanner.hasLoadedRequired())
        cookiesBanner.acceptCookies()
        shouldLoadRequired()
    }

    fun setLangIfNeeded(lang: String = SPConfig.lang) {
        if (SPConfig.model == "mobile" && !mobileMenu.selectedLang.text.contentEquals(lang, true)) {
            if (lang.contentEquals("en", ignoreCase = true)) {
                mobileMenu.langEn.click()
            } else {
                mobileMenu.langEs.click()
            }
        } else if (SPConfig.model != "mobile" && !desktopMenu.selectedLang.text.contentEquals(lang, true)) {
            if (lang.contentEquals("en", ignoreCase = true)) {
                desktopMenu.langEn.click()
            } else {
                desktopMenu.langEs.click()
            }
        }
        shouldLoadRequired(lang = lang)
        logger.info { "Set site language: $lang" }
    }
}

@Suppress("unused")
val mainFramePage = MainFramePage()
