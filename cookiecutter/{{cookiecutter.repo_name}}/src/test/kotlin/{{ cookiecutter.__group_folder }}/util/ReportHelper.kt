package {{ cookiecutter.group }}.util

import com.codeborne.selenide.Selenide
import io.qameta.allure.Allure.addAttachment
import org.openqa.selenium.OutputType

object ReportHelper {
    fun attachScreenshot(name: String = "Page screenshot") {
        val screenshot = Selenide.screenshot(OutputType.BYTES)?.inputStream()
        if (screenshot != null) addAttachment(name, screenshot) else addAttachment(name, "No screenshot available")
    }
}
