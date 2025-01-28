package {{ cookiecutter.group }}.common_win.util

import com.codeborne.selenide.Selenide
import io.qameta.allure.Allure
import org.openqa.selenium.OutputType

object AllureReportHelper {
    fun attachScreenshot(name: String = "Page screenshot") {
        val screenshot = Selenide.screenshot(OutputType.BYTES)?.inputStream()
        if (screenshot != null) screenshot.use { Allure.addAttachment(name, it) }
        else Allure.addAttachment(name, "No screenshot available")
    }
}
