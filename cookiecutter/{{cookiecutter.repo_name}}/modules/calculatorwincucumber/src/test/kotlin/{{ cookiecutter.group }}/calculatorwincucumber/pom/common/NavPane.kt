package {{ cookiecutter.group }}.calculatorwincucumber.pom.common

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class NavPane(self: SelenideElement) : Widget(self) {
    @Required val standard = find("#Standard")
    @Required val scientific = find("#Scientific")
    @Required val graphing = find("#Graphing")
    @Required val programmer = find("#Programmer")
    @Required val date = find("#Date")

    @Required val currency = find("#Currency")
    @Required val volume = find("#Volume")
    @Required val length = find("#Length")
    @Required val weight = find("#Weight")
    @Required val temperature = find("#Temperature")
    @Required val energy = find("#Energy")
    @Required val area = find("#Area")
    @Required val speed = find("#Speed")
    @Required val time = find("#Time")
    @Required val power = find("#Power")
    @Required val data = find("#Data")
    @Required val pressure = find("#Pressure")
    @Required val angle = find("#Angle")

    @Required val settings = find("#SettingsItem")
}
