package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.Selenide
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.Point
import org.openqa.selenium.Rectangle
import org.openqa.selenium.SearchContext
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.sikuli.script.Finder
import org.sikuli.script.Match
import org.sikuli.script.Pattern
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


class ByImage(
    private val imagePath: String,
    private val offsetX: Int = 0,
    private val offsetY: Int = 0,
    private val similarity: Double = 0.7,
) : By() {
    override fun findElement(context: SearchContext?): ImageWebElement {
        val screenshotStream = (context as TakesScreenshot).getScreenshotAs(OutputType.BYTES).inputStream()
        val screenshot = ImageIO.read(screenshotStream)
        val finder = Finder(screenshot)
        val pattern = Pattern(imagePath).similar(similarity).targetOffset(offsetX, offsetY)
        if (finder.find(pattern) == null) {
            throw RuntimeException("Find setup for image $imagePath with similarity $similarity is not possible in context $context")
        } else if (finder.hasNext()) {
            return ImageWebElement(finder.next(), context)
        } else {
            throw NoSuchElementException("Image $imagePath not found in context $context")
        }
    }

    override fun findElements(context: SearchContext?): List<ImageWebElement> {
        val screenshotStream = (context as TakesScreenshot).getScreenshotAs(OutputType.BYTES).inputStream()
        val screenshot = ImageIO.read(screenshotStream)
        val finder = Finder(screenshot)
        val pattern = Pattern(imagePath).similar(similarity)
        if (finder.find(pattern) == null) {
            throw RuntimeException("Find setup for image $imagePath with similarity $similarity is not possible in context $context")
        } else {
            return finder.list.map { ImageWebElement(it, context) }
        }
    }
}

class ImageWebElement(private val match: Match, private val context: SearchContext?) : WebElement {
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
        when (context) {
            is WebDriver -> {
                val x = match.center.x
                val y = match.center.y
            }

            is WebElement -> {
                val contextCenterX = context.size.width / 2
                val contextCenterY = context.size.height / 2
                val offsetX = contextCenterX + match.targetOffset.x
                val offsetY = contextCenterY + match.targetOffset.y
                Selenide.actions().moveToElement(context, offsetX, offsetY).click()
            }
        }
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
        throw UnsupportedOperationException("isSelected")
    }

    override fun isEnabled(): Boolean {
        throw UnsupportedOperationException("isEnabled")
    }

    override fun getText(): String {
        throw UnsupportedOperationException("getText")
    }

    override fun isDisplayed(): Boolean {
        return true
    }

    override fun getLocation(): Point {
        return Point(match.getX(), match.getY())
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

fun Rectangle.isContainedIn(other: Rectangle): Boolean {
    if (this.x < other.x) return false
    if (this.y < other.y) return false
    if (this.x + this.width > other.x + other.width) return false
    if (this.y + this.height > other.y + other.height) return false
    return true
}

fun Rectangle.contains(other: Rectangle): Boolean {
    return other.isContainedIn(this)
}
