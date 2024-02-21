package {{ cookiecutter.group }}.mtptestng.pom.common

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.visible
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import org.apache.logging.log4j.kotlin.Logging
import org.openqa.selenium.Keys
import org.openqa.selenium.interactions.Actions
import java.util.concurrent.TimeUnit

open class MainFramePage : Page(), Logging {
    @Required val home = find("a.img-menu")

    @Required(model = "desktop") val desktopMenu = DesktopMenuWidget(find("nav.menu-pc"))

    @Required(model = "mobile") val mobileMenu = MobileMenuWidget(find("div.menu-movil"))
    val mobileMenuPopUp = MobileMenuPopUpWidget(find("div#menu-movil"))
    val cookiesBanner = CookiesBannerWidget(find("div#CybotCookiebotDialog"))

    fun acceptCookies(lang: String = SPConfig.lang) {
        shouldLoadRequired(lang = lang)
        // This is what it should be, but for some reason, cookies message is not displayed when page loads.
        // It is displayed only when you "move the mouse" over the page.
        // shouldLoadRequired().cookiesBanner.acceptCookies()

        // Workaround
        when (SPConfig.model) {
            "mobile" -> acceptCookiesMobile(lang)
            "desktop" -> acceptCookiesDesktop(lang)
            else -> throw RuntimeException("Model ${SPConfig.model} not found")
        }
        shouldLoadRequired(lang = lang)
        logger.info { "Cookies accepted" }
    }

    private fun acceptCookiesDesktop(lang: String) {
        val driver = SPConfig.getWebDriver()!!
        val pressEnter = Actions(driver).moveToElement(desktopMenu.langEs).moveToElement(home).sendKeys("${Keys.ENTER}")
        pressEnter.perform()
        TimeUnit.SECONDS.sleep(1)
        for (retries in 1..5) {
            if (cookiesBanner.`is`(visible)) break
            // Click on home logo to trigger cookiesBanner
            pressEnter.perform()
            TimeUnit.SECONDS.sleep(1)
            logger.info { "Try number $retries to accept desktop cookies failed" }
        }
        cookiesBanner.shouldLoadRequired(lang = lang).acceptCookies(lang)
        logger.info { "Desktop cookies accepted" }
        cookiesBanner.should(disappear)
    }

    private fun acceptCookiesMobile(lang: String) {
        for (retries in 1..5) {
            if (cookiesBanner.`is`(visible)) break
            // Click on menu button to trigger cookiesBanner
            mobileMenu.mobileMenuButton.click(ClickOptions.withOffset(0, 30))
            TimeUnit.SECONDS.sleep(1)
            logger.info { "Try number $retries to accept mobile cookies failed" }
        }
        cookiesBanner.shouldLoadRequired(lang = lang).acceptCookies(lang)
        logger.info { "Mobile cookies accepted" }
        cookiesBanner.should(disappear)
    }

    fun setLangIfNeeded(lang: String = SPConfig.lang) {
        when (SPConfig.model) {
            "mobile" -> if (!mobileMenu.selectedLang.text.contentEquals(lang, true)) {
                when (lang) {
                    "en" -> mobileMenu.langEn.click()
                    "es" -> mobileMenu.langEs.click()
                    else -> throw RuntimeException("Language $lang not found")
                }
            }

            "desktop" -> if (!desktopMenu.selectedLang.text.contentEquals(lang, true)) {
                when (lang) {
                    "en" -> desktopMenu.langEn.click()
                    "es" -> desktopMenu.langEs.click()
                    else -> throw RuntimeException("Language $lang not found")
                }
            }

            else -> throw RuntimeException("Model ${SPConfig.model} not found")
        }
        shouldLoadRequired(lang = lang)
        logger.info { "Set site language: $lang" }
    }
}

@Suppress("unused") val mainFramePage = MainFramePage()
