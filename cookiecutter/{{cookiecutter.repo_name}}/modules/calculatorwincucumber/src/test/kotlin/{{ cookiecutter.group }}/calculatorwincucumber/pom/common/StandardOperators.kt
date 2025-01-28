package {{ cookiecutter.group }}.calculatorwincucumber.pom.common

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class StandardOperators(self: SelenideElement) : Widget(self) {
    @Required val divide = find("#divideButton")
    @Required val multiply = find("#multiplyButton")
    @Required val minus = find("#minusButton")
    @Required val plus = find("#plusButton")
    @Required val equal = find("#equalButton")

    val charToButtonMap = mapOf(
        '/' to divide,
        '*' to multiply,
        '-' to minus,
        '+' to plus,
        '=' to equal,
    )
}
