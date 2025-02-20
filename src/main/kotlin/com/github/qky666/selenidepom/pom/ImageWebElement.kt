package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.Selenide
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.Point
import org.openqa.selenium.Rectangle
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import org.sikuli.script.Match
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

private val logger = KotlinLogging.logger {}

class ImageWebElement(
    private val match: Match,
    private val context: SearchContext?,
    private val enabled: Boolean = true,
    private val selected: Boolean = false,
) : WebElement {
    override fun findElements(by: By): List<WebElement> {
        val elements = context?.findElements(by) ?: listOf()
        return elements.filter { it.rect.isContainedIn(this.rect) }
    }

    override fun findElement(by: By): WebElement {
        val elements = findElements(by)
        if (elements.isEmpty()) throw NoSuchElementException("Element not found in image ${match.name} using criteria $by")
        else return elements.first()
    }

    override fun <X : Any> getScreenshotAs(target: OutputType<X>): X {
        val byteArray = ByteArrayOutputStream().use {
            ImageIO.write(
                match.image.get().getSubimage(match.getX(), match.getY(), match.getW(), match.getH()), "png", it
            )
            it.toByteArray()
        }
        return target.convertFromPngBytes(byteArray)
    }

    override fun click() {
        val (x, y) = if (context is WebElement) {
            listOf(
                context.location.x + match.center.x + match.targetOffset.x,
                context.location.y + match.center.y + match.targetOffset.y
            )
        } else {
            listOf(match.center.x + match.targetOffset.x, match.center.y + match.targetOffset.y)
        }
        logger.debug { "Click point: ($x, $y)" }
        Selenide.actions().moveToLocation(x, y).click().perform()
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
        throw UnsupportedOperationException("getTagName")
    }

    @Deprecated("Deprecated in Java")
    override fun getAttribute(name: String): String? {
        throw UnsupportedOperationException("getAttribute")
    }

    override fun isSelected(): Boolean {
        return selected
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun getText(): String {
        throw UnsupportedOperationException("getText")
    }

    override fun isDisplayed(): Boolean {
        return true
    }

    override fun getLocation(): Point {
        val (x, y) = if (context is WebElement) {
            listOf(context.location.x + match.topLeft.x, context.location.y + match.topLeft.y)
        } else {
            listOf(match.topLeft.x, match.topLeft.y)
        }
        logger.debug { "Location point: ($x, $y)" }
        return Point(x, y)
    }

    override fun getSize(): Dimension {
        return Dimension(match.getW(), match.getH())
    }

    override fun getRect(): Rectangle {
        return Rectangle(location, size)
    }

    override fun getCssValue(propertyName: String): String {
        throw UnsupportedOperationException("getCssValue")
    }
}
