package {{ cookiecutter.group }}.calculatorwincucumber.pom.common

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class DisplayControls(self: SelenideElement) : Widget(self) {
    @Required val percent = find("#percentButton")
    @Required val clearEntry = find("#clearEntryButton")
    @Required val clear = find("#clearButton")
    @Required val backSpace = find("#backSpaceButton")
}
