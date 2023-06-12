package {{ cookiecutter.group }}.pom.common

import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.shouldLoadRequired

class CookiesBannerWidget(self: SelenideElement) : Widget(self) {

    @Required val cookiesText = LangConditionedElement(
        find("div.cli-bar-message"),
        mapOf("es" to text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)")),
        false
    )

    @Required val accept =
        LangConditionedElement(find("a#cookie_action_close_header"), mapOf("es" to text("Aceptar")), false)

    fun acceptCookies() {
        shouldLoadRequired().accept.click()
        should(disappear)
    }
}
