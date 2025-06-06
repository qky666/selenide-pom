package {{ cookiecutter.group }}.apidemosandroidcucumber.util

import com.codeborne.selenide.Selenide
import io.qameta.allure.Allure
import org.openqa.selenium.OutputType

@Suppress("unused")
object ReportHelper {
    fun attachScreenshot(name: String = "Page screenshot") {
        val screenshot = Selenide.screenshot(OutputType.BYTES)?.inputStream()
        if (screenshot != null) Allure.addAttachment(name, screenshot) else Allure.addAttachment(name, "No screenshot available")
    }
}
