package {{ cookiecutter.group }}.calculatorwincucumber.pom

import com.github.qky666.selenidepom.pom.Required
import {{ cookiecutter.group }}.calculatorwincucumber.pom.common.DisplayControls
import {{ cookiecutter.group }}.calculatorwincucumber.pom.common.HistoryMemoryPanel
import {{ cookiecutter.group }}.calculatorwincucumber.pom.common.MemoryPanel
import {{ cookiecutter.group }}.calculatorwincucumber.pom.common.NumberPad
import {{ cookiecutter.group }}.calculatorwincucumber.pom.common.StandardFunctions
import {{ cookiecutter.group }}.calculatorwincucumber.pom.common.StandardOperators
import kotlin.math.absoluteValue


@Suppress("MemberVisibilityCanBePrivate", "unused")
class StandardWindow : MainWindow() {
    @Required val alwaysOnTop = find("#NormalAlwaysOnTopButton")
    val expression = find("#CalculatorExpression")
    @Required val result = find("#CalculatorResults")
    @Required val memory = MemoryPanel(find("#MemoryPanel"))
    @Required val displayControls = DisplayControls(find("#DisplayControls"))
    @Required val standardFunctions = StandardFunctions(find("#StandardFunctions"))
    @Required val standardOperators = StandardOperators(find("#StandardOperators"))
    @Required val numberPad = NumberPad(find("#NumberPad"))
    @Required val negate = find("#negateButton")
    val historyMemoryPanel = HistoryMemoryPanel(find("#DockPanel"))

    fun write(number: Int) {
        number.absoluteValue.toString().forEach { numberPad.charToButtonMap[it]!!.click() }
        if (number < 0) negate.click()
    }
}

val standardWindow = StandardWindow()
