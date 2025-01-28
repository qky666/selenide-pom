package {{ cookiecutter.group }}.calculatorandroidcucumber.pom

import com.github.qky666.selenidepom.pom.LangConditionedAppiumElement
import com.github.qky666.selenidepom.pom.Required
import io.appium.java_client.AppiumBy.id

class ViewsDragAndDropPage : ApiDemosCommonFramePage() {
    @Required override val title = LangConditionedAppiumElement(super.title, "Views/Drag and Drop")
    @Required val dragExplanation = findAppium(id("io.appium.android.apis:id/drag_explanation"))
    @Required val dot1 = findAppium(id("io.appium.android.apis:id/drag_dot_1"))
    @Required val dot2 = findAppium(id("io.appium.android.apis:id/drag_dot_2"))
    @Required val dragResultText = findAppium(id("io.appium.android.apis:id/drag_result_text"))
    @Required val dot3 = findAppium(id("io.appium.android.apis:id/drag_dot_3"))
    @Required val dragText = findAppium(id("io.appium.android.apis:id/drag_text"))
}

val viewsDragAndDropPage = ViewsDragAndDropPage()
