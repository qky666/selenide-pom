package {{ cookiecutter.group }}.pom.common

import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import org.openqa.selenium.WebElement

class MobileMenuWidget(self: SelenideElement) : Widget(self) {
    @Required val mobileMenuButton = find("button.custom-menu-btn-flotante")

    @Required val languages = findAll("li.individual-menu-idioma>a")

    @Required val langEn = languages.findBy(text("en"))

    @Required val langEs = languages.findBy(text("es"))

    @Required val selectedLang = LangConditionedElement(
        find("li.individual-menu-idioma.idioma-activo>a"),
        mapOf("es" to "es", "en" to "en")
    )
}

class MobileMenuPopUpWidget(self: SelenideElement) : Widget(self) {
    // First level menu items

    // All first level menu items
    @Required val firstLevelMenuItems = findAll("li.uk-parent")

    @Required
    @JvmOverloads
    fun services(lang: String = SPConfig.lang): SelenideElement {
        return when (lang) {
            "en" -> firstLevelMenuItems.findBy(exactText("Services"))
            "es" -> firstLevelMenuItems.findBy(exactText("Servicios"))
            else -> throw RuntimeException("Language $lang not found")
        }
    }

    @Required
    @JvmOverloads
    fun areas(lang: String = SPConfig.lang): SelenideElement {
        return when (lang) {
            "en" -> firstLevelMenuItems.findBy(exactText("Areas"))
            "es" -> firstLevelMenuItems.findBy(exactText("Sectores"))
            else -> throw RuntimeException("Language $lang not found")
        }
    }

    // Possible bug in mtp.es in english, it only appears on desktop view
    @Required(lang = "es")
    @JvmOverloads
    fun training(lang: String = SPConfig.lang): SelenideElement {
        return when (lang) {
            "en" -> firstLevelMenuItems.findBy(exactText("Training"))
            "es" -> firstLevelMenuItems.findBy(exactText("Formación"))
            else -> throw RuntimeException("Language $lang not found")
        }
    }

    @Required(lang = "es")
    val blog = firstLevelMenuItems.findBy(exactText("Blog"))

    @Required
    @JvmOverloads
    fun talent(lang: String = SPConfig.lang): SelenideElement {
        return when (lang) {
            "en" -> firstLevelMenuItems.findBy(exactText("Talent"))
            "es" -> firstLevelMenuItems.findBy(exactText("Talento"))
            else -> throw RuntimeException("Language $lang not found")
        }
    }

    @Required
    @JvmOverloads
    fun about(lang: String = SPConfig.lang): SelenideElement {
        return when (lang) {
            "en" -> firstLevelMenuItems.findBy(exactText("About MTP"))
            "es" -> firstLevelMenuItems.findBy(exactText("Sobre MTP"))
            else -> throw RuntimeException("Language $lang not found")
        }
    }

    @Required
    @JvmOverloads
    fun contact(lang: String = SPConfig.lang): SelenideElement {
        return when (lang) {
            "en" -> findAll("li>a").findBy(exactText("Contact Us"))
            "es" -> findAll("li>a").findBy(exactText("Contacto"))
            else -> throw RuntimeException("Language $lang not found")
        }
    }

    // Second level menú items. I only write one, but there are more
    fun servicesQualityAssurance(lang: String = SPConfig.lang): SelenideElement {
        return when (lang) {
            "en" -> findAll("a").findBy(exactText("Quality Assurance"))
            "es" -> findAll("a").findBy(exactText("Aseguramiento de la calidad"))
            else -> throw RuntimeException("Language $lang not found")
        }
    }

    fun shouldBeCollapsed() {
        firstLevelMenuItems.shouldHave(
            CollectionCondition.allMatch("All firstLevelMenuItems have aria-expanded=false") { element: WebElement ->
                "false".equals(element.getAttribute("aria-expanded"), ignoreCase = true)
            }
        )
    }
}
