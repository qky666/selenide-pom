package com.github.qky666.selenidepom.pom

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.SearchContext
import org.openqa.selenium.TakesScreenshot
import org.sikuli.script.Finder
import org.sikuli.script.Pattern
import java.io.File
import javax.imageio.ImageIO

private val logger = KotlinLogging.logger {}

class ByImage(
    private val imagePath: String,
    private val offsetX: Int = 0,
    private val offsetY: Int = 0,
    private val similarity: Double = 0.7,
) : By() {
    override fun findElement(context: SearchContext?): ImageWebElement {
        val screenshotStream = (context as TakesScreenshot).getScreenshotAs(OutputType.BYTES).inputStream()
        val screenshot = ImageIO.read(screenshotStream)
        // Debug
        ImageIO.write(screenshot, "png", File("build/tmp/screenshot.png"))
        val finder = Finder(screenshot)
        val pattern = Pattern(imagePath).similar(similarity).targetOffset(offsetX, offsetY)
        if (finder.find(pattern) == null) {
            throw RuntimeException("Find setup for image $imagePath with similarity $similarity is not possible in context $context")
        } else if (finder.hasNext()) {
            val match = finder.next()
            logger.debug { "Match: (${match.x}, ${match.y}. Match score: ${match.score}" }
            return ImageWebElement(match, context)
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

