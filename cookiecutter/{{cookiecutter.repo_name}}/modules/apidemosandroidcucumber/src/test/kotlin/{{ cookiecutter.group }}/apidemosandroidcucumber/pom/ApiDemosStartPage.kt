package {{ cookiecutter.group }}.apidemosandroidcucumber.pom

import com.github.qky666.selenidepom.pom.Required
import io.appium.java_client.AppiumBy.accessibilityId

class ApiDemosStartPage : ApiDemosCommonFramePage() {
    @Required val accessibility = findAppium(accessibilityId("Accessibility"))
    @Required val animation = findAppium(accessibilityId("Animation"))
    @Required val app = findAppium(accessibilityId("App"))
    @Required val content = findAppium(accessibilityId("Content"))
    @Required val graphics = findAppium(accessibilityId("Graphics"))
    @Required val media = findAppium(accessibilityId("Media"))
    @Required val nfc = findAppium(accessibilityId("NFC"))
    @Required val os = findAppium(accessibilityId("OS"))
    @Required val preference = findAppium(accessibilityId("Preference"))
    @Required val text = findAppium(accessibilityId("Text"))
    @Required val views = findAppium(accessibilityId("Views"))
}

val apiDemosStartPage = ApiDemosStartPage()
