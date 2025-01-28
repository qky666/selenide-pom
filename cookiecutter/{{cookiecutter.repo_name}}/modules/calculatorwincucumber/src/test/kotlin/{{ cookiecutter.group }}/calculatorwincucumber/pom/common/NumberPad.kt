package {{ cookiecutter.group }}.calculatorwincucumber.pom.common

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

@Suppress("MemberVisibilityCanBePrivate")
class NumberPad(self: SelenideElement) : Widget(self) {
    @Required val one = Page.find("#num1Button")
    @Required val two = Page.find("#num2Button")
    @Required val three = Page.find("#num3Button")
    @Required val four = Page.find("#num4Button")
    @Required val five = Page.find("#num5Button")
    @Required val six = Page.find("#num6Button")
    @Required val seven = Page.find("#num7Button")
    @Required val eight = Page.find("#num8Button")
    @Required val nine = Page.find("#num9Button")
    @Required val zero = Page.find("#num0Button")
    @Required val decimalSeparator = Page.find("#decimalSeparatorButton")

    val charToButtonMap = mapOf(
        '0' to zero,
        '1' to one,
        '2' to two,
        '3' to three,
        '4' to four,
        '5' to five,
        '6' to six,
        '7' to seven,
        '8' to eight,
        '9' to nine,
        ',' to decimalSeparator,
        '.' to decimalSeparator,
    )
}
