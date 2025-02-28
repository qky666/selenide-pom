package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.Selenide
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.global.opencv_imgcodecs.imwrite
import org.bytedeco.opencv.opencv_core.Mat
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.Point
import org.openqa.selenium.Rectangle
import org.openqa.selenium.WebElement
import java.nio.file.Files
import kotlin.io.path.deleteIfExists
import org.bytedeco.opencv.opencv_core.Rect as CVRect

/**
 * Represents a [WebElement] defined by an image that has been found inside a web page (or a [WebElement]) screenshot.
 *
 * @param container the smallest [WebElement] that contains the image found
 * @param matchRect the [Rectangle] where the image has been found inside the web page
 * @param enabled if the found element is considered enabled or not
 * @param selected if the found element is considered selected or not
 */
class ImageWebElement(
    private val container: WebElement,
    private val matchRect: Rectangle,
    private val enabled: Boolean = true,
    private val selected: Boolean = false,
) : WebElement by container {

    private val center: Point
        get() = Point(matchRect.x + matchRect.width / 2, matchRect.y + matchRect.height / 2)

    private val matchCVRectInContainer: CVRect
        get() = CVRect(
            matchRect.x - container.location.x, matchRect.y - container.location.y, matchRect.width, matchRect.height
        )

    override fun click() {
        Selenide.actions().moveToLocation(center.x, center.y).click().perform()
    }

    override fun isSelected(): Boolean {
        return selected
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun findElements(by: By): List<WebElement> {
        val elements = container.findElements(by)
        return elements.filter { it.rect.isContainedIn(this.rect) }
    }

    override fun findElement(by: By): WebElement {
        val elements = findElements(by)
        if (elements.isEmpty()) throw NoSuchElementException("Element not found in image using criteria $by")
        else return elements.first()
    }

    override fun isDisplayed(): Boolean {
        return true
    }

    override fun getLocation(): Point {
        return matchRect.point
    }

    override fun getSize(): Dimension {
        return Dimension(matchRect.width, matchRect.height)
    }

    override fun getRect(): Rectangle {
        return matchRect
    }

    override fun <X : Any> getScreenshotAs(target: OutputType<X>): X {
        val containerScreenshotFile = container.getScreenshotAs(OutputType.FILE)
        val containerScreenshot = imread(containerScreenshotFile.path.toString())
        val matchScreenshot = Mat(containerScreenshot, matchCVRectInContainer)
        val tempFile = kotlin.io.path.createTempFile("match", ".png")
        imwrite(tempFile.toAbsolutePath().toString(), matchScreenshot)
        val bytes = Files.readAllBytes(tempFile)
        tempFile.deleteIfExists()
        return target.convertFromPngBytes(bytes)
    }
}
