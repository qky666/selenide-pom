package {{ cookiecutter.group }}.apidemosandroidcucumber.testng

import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.DragAndDropOptions
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.appium.SelenideAppium
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import {{ cookiecutter.group }}.apidemosandroidcucumber.pom.apiDemosStartPage
import {{ cookiecutter.group }}.apidemosandroidcucumber.pom.viewsDragAndDropPage
import {{ cookiecutter.group }}.apidemosandroidcucumber.pom.viewsPage
import {{ cookiecutter.group }}.common_android.util.AllureReportHelper
import org.apache.logging.log4j.kotlin.Logging
import org.testng.ITestResult
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

open class AppiumTest : Logging {

    @BeforeMethod(description = "Run app", alwaysRun = true)
    fun beforeMethod() {
        SPConfig.resetConfig()

        // Set env
        TestData.init("test")

        SPConfig.quitDriver()
        SPConfig.selenideConfig.browserSize(null)
        SPConfig.setDriver()

        // Start app
        SelenideAppium.launchApp()
    }

    @AfterMethod(description = "Close webdriver", alwaysRun = true)
    fun afterMethod(result: ITestResult) {
        // Attach screenshot
        if (result.status != ITestResult.SUCCESS) {
            AllureReportHelper.attachScreenshot("Test failed screenshot")
        }

        // Quit webdriver
        Selenide.closeWebDriver()
        logger.info { "Closed webdriver for test ${result.name}. Status: ${result.status}" }
    }

    @Test(description = "Drag and drop test")
    fun dragAndDropTest() {
        // chooseApiDemosAccessPage.shouldLoadRequired().continueButton.click()
        apiDemosStartPage.shouldLoadRequired()
        apiDemosStartPage.views.click()

        viewsPage.shouldLoadRequired()
        viewsPage.dragAndDrop.click()

        viewsDragAndDropPage.shouldLoadRequired()
        viewsDragAndDropPage.dragText.shouldHave(exactText(""))
        viewsDragAndDropPage.dot1.dragAndDrop(DragAndDropOptions.to(viewsDragAndDropPage.dot2).usingSeleniumActions())
        viewsDragAndDropPage.dragText.shouldBe(visible).shouldHave(text("Dot"), text("DraggableDot"))
    }

    @Test(description = "Drag and drop test - forced error")
    fun dragAndDropTestForcedError() {
        // chooseApiDemosAccessPage.shouldLoadRequired().continueButton.click()
        apiDemosStartPage.shouldLoadRequired()
        apiDemosStartPage.views.click()

        viewsPage.shouldLoadRequired()
        viewsPage.dragAndDrop.click()

        viewsDragAndDropPage.shouldLoadRequired()
        viewsDragAndDropPage.dragText.shouldHave(exactText(""))
        viewsDragAndDropPage.dot1.dragAndDrop(DragAndDropOptions.to(viewsDragAndDropPage.dot2).usingSeleniumActions())
        viewsDragAndDropPage.dragText.shouldBe(visible).shouldHave(text("Dot"), text("Bad text"))
    }
}
