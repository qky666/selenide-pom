@file:Suppress("unused")

package {{ cookiecutter.group }}.mtptestng.pom.common

import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.shouldLoadRequired

class CookiesBannerWidget(self: SelenideElement) : Widget(self) {

    val decline = LangConditionedElement(
        find("button#CybotCookiebotDialogBodyButtonDecline"),
        mapOf("es" to text("Denegar")),
        false,
    )
    @Required val accept = LangConditionedElement(
        find("button#CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll"),
        mapOf("es" to text("Permitir todas")),
        false,
    )

    fun acceptCookies(lang: String = SPConfig.lang) {
        shouldLoadRequired(lang = lang).accept.click()
        should(disappear)
    }
}
