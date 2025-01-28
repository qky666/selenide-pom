package {{ cookiecutter.group }}.calculatorwincucumber.pom.common

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class HistoryMemoryPanel(self: SelenideElement) : Widget(self) {
    @Required val historyTabItem = find("#HistoryLabel")
    @Required val memoryTabItem = find("#MemoryLabel")
}
