package {{ cookiecutter.group }}.pom.common

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.Condition.disappear
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.hasLoadedRequired
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import org.apache.logging.log4j.kotlin.Logging
import java.time.Duration

open class MainFramePage : Page(), Logging {
    @Required val home = find("a.img-menu")

    @Required(model = "desktop")
    val desktopMenu = DesktopMenuWidget(find("nav.menu-pc"))

    @Required(model = "mobile")
    val mobileMenu = MobileMenuWidget(find("div.menu-movil"))
    val mobileMenuPopUp = MobileMenuPopUpWidget(find("div#menu-movil"))
    val cookiesBanner = CookiesBannerWidget(find("div#CybotCookiebotDialog"))

    fun acceptCookies() {
        // This is what it should be, but for some reason, cookies message is not displayed when page loads.
        // It is displayed only when you "move the mouse" over the page.
        // shouldLoadRequired().cookiesBanner.acceptCookies()

        // Workaround
        when (SPConfig.model) {
            "mobile" -> acceptCookiesMobile()
            "desktop" -> acceptCookiesDesktop()
            else -> throw RuntimeException("Model ${SPConfig.model} not found")
        }
        logger.info { "Cookies accepted" }
    }

    private fun acceptCookiesDesktop() {
        for (retries in 1..5) {
            if (cookiesBanner.hasLoadedRequired(Duration.ofSeconds(1))) {
                cookiesBanner.acceptCookies()
                break
            }
            // Click on search button to trigger cookiesBanner
            desktopMenu.searchOpen.click()
            desktopMenu.langEs.click(ClickOptions.withOffset(0, -50))
        }
        cookiesBanner.should(disappear)
        shouldLoadRequired()
    }

    private fun acceptCookiesMobile() {
        shouldLoadRequired()
        for (retries in 1..5) {
            if (cookiesBanner.hasLoadedRequired(Duration.ofSeconds(1))) {
                cookiesBanner.acceptCookies()
                break
            }
            // Click on menu button to trigger cookiesBanner
            mobileMenu.mobileMenuButton.click(ClickOptions.withOffset(0, 30))
        }
        cookiesBanner.should(disappear)
        shouldLoadRequired()
    }

    fun setLangIfNeeded(lang: String = SPConfig.lang) {
        when (SPConfig.model) {
            "mobile" -> {
                if (!mobileMenu.selectedLang.text.contentEquals(lang, true)) {
                    when (lang) {
                        "en" -> mobileMenu.langEn.click()
                        "es" -> mobileMenu.langEs.click()
                        else -> throw RuntimeException("Language $lang not found")
                    }
                }
            }
            "desktop" -> {
                if (!desktopMenu.selectedLang.text.contentEquals(lang, true)) {
                    when (lang) {
                        "en" -> desktopMenu.langEn.click()
                        "es" -> desktopMenu.langEs.click()
                        else -> throw RuntimeException("Language $lang not found")
                    }
                }
            }
            else -> throw RuntimeException("Model ${SPConfig.model} not found")
        }
        shouldLoadRequired(lang = lang)
        logger.info { "Set site language: $lang" }
    }
}

@Suppress("unused")
val mainFramePage = MainFramePage()
