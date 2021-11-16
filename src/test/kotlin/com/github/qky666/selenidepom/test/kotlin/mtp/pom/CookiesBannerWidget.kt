package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.kotlin.Required
import com.github.qky666.selenidepom.kotlin.RequiredError
import com.github.qky666.selenidepom.kotlin.Widget
import java.time.Duration
import kotlin.Throws

class CookiesBannerWidget(override val self: SelenideElement = Selenide.element("div.cookie-notice-container")) :
    Widget(self) {

    @Required val cookiesText = self.find("span#cn-notice-text")
    @Required val accept = self.find("a#cn-accept-cookie")
    @Required val close = self.find("a#cn-close-notice")

    @Throws(RequiredError::class)
    override fun shouldLoadRequired(timeout: Duration) {
        super.shouldLoadRequired(timeout)
        cookiesText.shouldHave(Condition.text("Utilizamos cookies para asegurar que damos la mejor experiencia al usuario en nuestra web. Si sigues utilizando este sitio asumiremos que estás de acuerdo."))
    }

    fun acceptCookies() {
        shouldLoadRequired()
        accept.click()
        self.should(Condition.disappear)
    }

    fun acceptCookiesIfDisplayed() {
        if (self.isDisplayed) {
            acceptCookies()
        }
    }
}
