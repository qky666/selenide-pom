package {{ cookiecutter.group }}.pom.pages.home

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import {{ cookiecutter.group }}.pom.common.MainFramePage

class HomePage : MainFramePage() {
    @Required val mainBanner = MainBannerWidget(find("div.custom-bg-primary"))

    fun open() {
        Selenide.open(TestData.input.getProperty("data.input.baseUrl"))
        shouldLoadRequired(lang = "es")
    }
}

class MainBannerWidget(self: SelenideElement) : Widget(self) {
    @Required val title = LangConditionedElement(
        find("h1"),
        mapOf(
            "es" to "MTP, referencia en aseguramiento de negocios digitales",
            "en" to "MTP ensures quality digital public services"
        )
    )

    @Required val text = LangConditionedElement(
        find("p"),
        mapOf(
            "es" to "Acompañamos a nuestros clientes en su transformación digital y asegurando la calidad de software, experiencia de usuario, seguridad y los desarrollos durante todo el ciclo de vida.",
            "en" to "MTP, the drive for the digital transformation of public administrations"
        )
    )

    @Required val moreInfo = LangConditionedElement(
        find("a"),
        mapOf(
            "es" to "Más información",
            "en" to "More information"
        )
    )
}

val homePage = HomePage()
