package {{ cookiecutter.group }}.calculatorwincucumber.pom

import com.codeborne.selenide.Condition.disappear
import com.codeborne.selenide.Condition.visible
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import {{ cookiecutter.group }}.calculatorwincucumber.pom.common.NavPane


@Suppress("MemberVisibilityCanBePrivate", "unused")
open class MainWindow : Page() {
    @Required val togglePane = find("#TogglePaneButton")

    val navPane = NavPane(find("#PaneRoot"))

    fun displayNavPane(value: Boolean = true) {
        if (navPane.isDisplayed != value) togglePane.click()
        (if (value) visible else disappear).let { navPane.should(it) }
    }
}
