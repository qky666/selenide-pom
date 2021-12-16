package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.Required
import com.github.qky666.selenidepom.RequiredError
import com.github.qky666.selenidepom.Widget
import java.time.Duration
import kotlin.Throws

class CookiesBannerWidget(override val self: SelenideElement = Selenide.element("div#cookie-law-info-bar")) :
    Widget(self) {

    @Required val cookiesText = self.find("div.cli-bar-message")
    @Required val accept = self.find("a#cookie_action_close_header")

    @Throws(RequiredError::class)
    override fun shouldLoadRequired(timeout: Duration, pomVersion: String) {
        super.shouldLoadRequired(timeout, pomVersion)
        cookiesText.shouldHave(Condition.text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)"))
    }

    fun acceptCookies() {
        shouldLoadRequired()
        accept.click()
        self.should(Condition.disappear)
    }
}
