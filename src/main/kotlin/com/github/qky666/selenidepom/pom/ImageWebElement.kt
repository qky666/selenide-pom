package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.Selenide
import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.opencv.global.opencv_imgcodecs.imwrite
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.Rect as CVRect
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.Point
import org.openqa.selenium.Rectangle
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import java.nio.file.Files
import kotlin.io.path.deleteIfExists

private val logger = KotlinLogging.logger {}

/**
 * Represents a [WebElement] defined by an image that has been found inside a search [context] screenshot.
 *
 * @param screenshot the screenshot where the image has been found
 * @param matchRect the [CVRect] where the image has been found inside the screenshot
 * @param context the [SearchContext], usually a [WebElement] or a `WebDriver`
 * @param enabled if the found element is considered enabled or not
 * @param selected if the found element is considered selected or not
 */
class ImageWebElement(
    private val screenshot: Mat,
    private val matchRect: CVRect,
    private val context: SearchContext?,
    private val enabled: Boolean = true,
    private val selected: Boolean = false,
) : WebElement {

    private val center: Point
        get() = if (context is WebElement) {
            Point(
                context.location.x + matchRect.x() + matchRect.width() / 2,
                context.location.y + matchRect.y() + matchRect.height() / 2,
            )
        } else {
            Point(matchRect.x() + matchRect.width() / 2, matchRect.y() + matchRect.height() / 2)
        }

    override fun findElements(by: By): List<WebElement> {
        val elements = context?.findElements(by) ?: listOf()
        return elements.filter { it.rect.isContainedIn(this.rect) }
    }

    override fun findElement(by: By): WebElement {
        val elements = findElements(by)
        if (elements.isEmpty()) throw NoSuchElementException("Element not found in image using criteria $by")
        else return elements.first()
    }

    override fun <X : Any> getScreenshotAs(target: OutputType<X>): X {
        val matchScreenshot = Mat(screenshot, matchRect)
        val tempFile = kotlin.io.path.createTempFile("match", ".png")
        imwrite(tempFile.toAbsolutePath().toString(), matchScreenshot)
        val bytes = Files.readAllBytes(tempFile)
        tempFile.deleteIfExists()
        return target.convertFromPngBytes(bytes)
    }

    override fun click() {
        Selenide.actions().moveToLocation(center.x, center.y).click().perform()
    }

    override fun submit() {
        throw UnsupportedOperationException("submit")
    }

    override fun sendKeys(vararg keysToSend: CharSequence) {
        throw UnsupportedOperationException("sendKeys")
    }

    override fun clear() {
        throw UnsupportedOperationException("clear")
    }

    override fun getTagName(): String {
        return ""
    }

    @Deprecated("Deprecated in Java", ReplaceWith("getDomAttribute"))
    override fun getAttribute(name: String): String? {
        return null
    }

    override fun isSelected(): Boolean {
        return selected
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun getText(): String {
        return ""
    }

    override fun isDisplayed(): Boolean {
        return true
    }

    override fun getLocation(): Point {
        val (x, y) = if (context is WebElement) {
            listOf(context.location.x + matchRect.x(), context.location.y + matchRect.y())
        } else {
            listOf(matchRect.x(), matchRect.y())
        }
        logger.debug { "Location point: ($x, $y)" }
        return Point(x, y)
    }

    override fun getSize(): Dimension {
        return Dimension(matchRect.width(), matchRect.height())
    }

    override fun getRect(): Rectangle {
        return Rectangle(location, size)
    }

    override fun getCssValue(propertyName: String): String {
        return ""
    }
}
