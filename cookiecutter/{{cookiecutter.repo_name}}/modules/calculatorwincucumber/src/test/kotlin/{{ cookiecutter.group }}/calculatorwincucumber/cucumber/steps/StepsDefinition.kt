@file:Suppress("unused")

package {{ cookiecutter.group }}.calculatorwincucumber.cucumber.steps

import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.appium.SelenideAppium
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import {{ cookiecutter.group }}.calculatorwincucumber.pom.MainWindow
import {{ cookiecutter.group }}.calculatorwincucumber.pom.StandardWindow
import io.cucumber.java8.Es
import org.apache.logging.log4j.kotlin.Logging
import java.util.concurrent.TimeUnit

class StepsDefinition : Es, Logging {

    init {
        Dado("Se abre la aplicación Calculator") {
            SelenideAppium.launchApp()
            Page.getInstance(MainWindow::class).shouldLoadRequired()
        }

        Dado("Se selecciona Calculadora Estándar") {
            Page.getInstance(MainWindow::class).let {
                it.shouldLoadRequired().displayNavPane()
                it.navPane.standard.click()
                it.navPane.should(disappear)
            }
            Page.getInstance(StandardWindow::class).shouldLoadRequired()
        }

        Dado("Se escribe el número {int} en la Calculadora Estándar") { number: Int ->
            Page.getInstance(StandardWindow::class).shouldLoadRequired().write(number)
        }

        Dado("Se pulsa en el botón + de la Calculadora Estándar") {
            Page.getInstance(StandardWindow::class).shouldLoadRequired().standardOperators.plus.click()
        }

        Dado("Se pulsa en el botón = de la Calculadora Estándar") {
            Page.getInstance(StandardWindow::class).shouldLoadRequired().standardOperators.equal.click()
        }

        Dado("Se verifica que el resultado es {string} en la Calculadora Estándar") { value: String ->
            Page.getInstance(StandardWindow::class)
                .shouldLoadRequired().result.shouldHave(exactText("La pantalla muestra $value"))
        }

        Dado("Se verifica que la expresión es {string} en la Calculadora Estándar") { value: String ->
            Page.getInstance(StandardWindow::class)
                .shouldLoadRequired().expression.shouldHave(exactText("La expresión es $value"))
        }

        Dado("Se cierra la ventana") {
            SPConfig.quitDriver()
            TimeUnit.SECONDS.sleep(5)
        }
    }
}
