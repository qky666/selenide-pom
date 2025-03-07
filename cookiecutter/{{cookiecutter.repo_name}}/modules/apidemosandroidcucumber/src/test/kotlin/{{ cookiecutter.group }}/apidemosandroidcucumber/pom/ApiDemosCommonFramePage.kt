package {{ cookiecutter.group }}.apidemosandroidcucumber.pom

import com.github.qky666.selenidepom.pom.LangConditionedAppiumElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required

open class ApiDemosCommonFramePage : Page() {
    @Required open val title = LangConditionedAppiumElement(
        findAppium(".//*[@resource-id='android:id/action_bar']/android.widget.TextView"), "API Demos"
    )
}
