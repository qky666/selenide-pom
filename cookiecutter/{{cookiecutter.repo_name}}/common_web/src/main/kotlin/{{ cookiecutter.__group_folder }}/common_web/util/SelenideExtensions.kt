package {{ cookiecutter.group }}.common_web.util

import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.Keys

fun SelenideElement.delete(times: Int = 0) {
    if (times == 0) {
        while (!value.isNullOrBlank()) {
            click()
            sendKeys(Keys.DELETE)
            sendKeys(Keys.BACK_SPACE)
        }
    } else {
        repeat(times) {
            click()
            sendKeys(Keys.DELETE)
            sendKeys(Keys.BACK_SPACE)
        }
    }
}
