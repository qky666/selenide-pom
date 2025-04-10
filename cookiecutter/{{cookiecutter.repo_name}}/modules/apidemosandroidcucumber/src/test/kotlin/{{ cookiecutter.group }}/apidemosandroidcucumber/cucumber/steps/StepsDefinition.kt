@file:Suppress("unused")

package {{ cookiecutter.group }}.apidemosandroidcucumber.cucumber.steps

import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.DragAndDropOptions
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.appium.SelenideAppium
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import {{ cookiecutter.group }}.apidemosandroidcucumber.pom.ApiDemosStartPage
import {{ cookiecutter.group }}.apidemosandroidcucumber.pom.ViewsDragAndDropPage
import {{ cookiecutter.group }}.apidemosandroidcucumber.pom.ViewsPage
import io.cucumber.java8.Es
import org.apache.logging.log4j.kotlin.Logging
import java.util.concurrent.TimeUnit

class StepsDefinition : Es, Logging {

    init {
        Dado("Se abre la app ApiDemos") {
            SelenideAppium.launchApp()
            Page.getInstance(ApiDemosStartPage::class).shouldLoadRequired()
        }

        Dado("Se selecciona Views -> Drag And Drop") {
            Page.getInstance(ApiDemosStartPage::class).shouldLoadRequired().views.click()
            Page.getInstance(ViewsPage::class).shouldLoadRequired().dragAndDrop.click()
        }

        Dado("Se arrastra el Punto1 sobre el Punto2 en Drag & Drop") {
            Page.getInstance(ViewsDragAndDropPage::class).let {
                it.shouldLoadRequired().dot1.dragAndDrop(
                    DragAndDropOptions.to(it.dot2).usingSeleniumActions()
                )
            }
        }

        Dado("Se verifica que se muestra el texto exacto {string} en Drag And Drop") { value: String ->
            Page.getInstance(ViewsDragAndDropPage::class).shouldLoadRequired().dragText.shouldBe(visible)
                .shouldHave(exactText(value))
        }

        Dado("Se verifica que se muestra el texto {string} en Drag And Drop") { value: String ->
            Page.getInstance(ViewsDragAndDropPage::class).shouldLoadRequired().dragText.shouldBe(visible)
                .shouldHave(text(value))
        }

        Dado("Se cierra la app") {
//            TestData.getString("project.app_package")?.let { SelenideAppium.terminateApp(it) }
            Selenide.closeWebDriver()
            TimeUnit.SECONDS.sleep(5)
        }
    }
}
