package {{ cookiecutter.group }}.calculatorwincucumber.pom.common

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class StandardFunctions(self: SelenideElement) : Widget(self) {
    @Required val invert = find("#invertButton")
    @Required val power2 = find("#xpower2Button")
    @Required val squareRoot = find("#squareRootButton")
}
