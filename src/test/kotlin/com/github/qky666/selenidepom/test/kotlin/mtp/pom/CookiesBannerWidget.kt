package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import java.time.Duration

class CookiesBannerWidget(self: SelenideElement) : Widget(self) {

    @Required val cookiesText = find("div.cli-bar-message")
    @Required val accept = find("a#cookie_action_close_header")

    override fun customShouldLoadRequired(timeout: Duration, pomVersion: String) {
        super.customShouldLoadRequired(timeout, pomVersion)
        cookiesText.shouldHave(
            text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)"),
            timeout
        )
    }

    fun acceptCookies() {
        shouldLoadRequired().accept.click()
        should(disappear)
    }
}
