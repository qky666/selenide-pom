package {{ cookiecutter.group }}.calculatorwincucumber.pom.common

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class MemoryPanel(self: SelenideElement) : Widget(self) {
    @Required val clearMemory = find("#ClearMemoryButton")
    @Required val memRecall = find("#MemRecall")
    @Required val memPlus = find("#MemPlus")
    @Required val memMinus = find("#MemMinus")
    @Required val memButton = find("#memButton")
}
