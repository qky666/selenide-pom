package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebDriverRunner
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Abstract class that represents an Iframe, interpreted as a section of a web page.
 * Given the special treatment of Iframes in Selenium, 'find' methods in [Iframe] do not search inside `self`,
 * but in the whole page. You should be aware of using these methods inside a [switch] block.
 *
 * @param self the Iframe ([SelenideElement]) in the web page
 * @constructor creates a new instance using provided [SelenideElement]
 */
abstract class Iframe(val self: SelenideElement) : Widget(self) {
    override val container = null
}

/**
 * Switch to the Iframe and executes the given block function. Then switch back to the parent frame.
 * See: [org.openqa.selenium.WebDriver.switchTo]
 *
 * @param block a function to run while switched into the Iframe
 * @return the result of block function invoked on this resource
 */
fun <T : Iframe, R> T.switch(block: (T) -> R): R {
    val frameLog = this.toString().replace("\n", "\\n")
    logger.info { "Switching into frame $frameLog" }
    val driver = WebDriverRunner.getWebDriver()
    try {
        driver.switchTo().frame(self)
        return block(this)
    } catch (e: Throwable) {
        throw e
    } finally {
        logger.info { "Switching out of frame $frameLog" }
        driver.switchTo().parentFrame()
    }
}
