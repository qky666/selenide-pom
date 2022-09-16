package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.annotation.Required
import com.github.qky666.selenidepom.error.RequiredError
import com.github.qky666.selenidepom.pom.Widget
import java.time.Duration
import kotlin.Throws

class CookiesBannerWidget(self: SelenideElement) : Widget<CookiesBannerWidget>(self) {

    @Required val cookiesText = this.find("div.cli-bar-message")
    @Required val accept = this.find("a#cookie_action_close_header")

    @Throws(RequiredError::class)
    override fun shouldLoadRequired(timeout: Duration, pomVersion: String) {
        super.shouldLoadRequired(timeout, pomVersion)
        cookiesText.shouldHave(Condition.text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)"))
    }

    fun acceptCookies() {
        shouldLoadRequired()
        accept.click()
        this.should(Condition.disappear)
    }
}
