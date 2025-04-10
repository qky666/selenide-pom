/************
 * NOT USED!
 ************/

package {{ cookiecutter.group }}.apidemosandroidcucumber.pom

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedAppiumElement
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection
import io.appium.java_client.AppiumBy.id


@Suppress("unused")
class ChooseApiDemosAccessPage : Page() {
    @Required val title = LangConditionedAppiumElement(
        findAppium(id("com.android.permissioncontroller:id/permissions_message")),
        "Choose what to allow API Demos to access"
    )

    @Required val permissions = WidgetsCollection(
        findXAll(".//androidx.recyclerview.widget.RecyclerView[@resource-id='com.android.permissioncontroller:id/recycler_view']/android.widget.LinearLayout"),
        ::SingleAccessPermissionWidget
    )

    @Required val cancelButton = findAppium(id("com.android.permissioncontroller:id/cancel_button"))
    @Required val continueButton = findAppium(id("com.android.permissioncontroller:id/continue_button"))
}

class SingleAccessPermissionWidget(self: SelenideElement) : Widget(self) {
    @Required val icon = find(id("com.android.permissioncontroller:id/icon_frame"))
    @Required val title = find(id("android:id/title"))
    @Required val summary = find(id("android:id/summary"))
    @Required val switch = find(id("android:id/switch_widget"))
}
