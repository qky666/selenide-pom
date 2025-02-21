package com.github.qky666.selenidepom.pom

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.SearchContext
import org.openqa.selenium.TakesScreenshot
import org.sikuli.script.Finder
import org.sikuli.script.Pattern
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.toPath
import kotlin.test.assertTrue

class ByImage(
    private val imageElementDefinitions: List<ImageElementDefinition>,
    private val offsetX: Int = 0,
    private val offsetY: Int = 0,
    private val similarity: Double = DEFAULT_SIMILARITY,
) : By() {
    private val logger = KotlinLogging.logger {}

    constructor(imagePath: Path, offsetX: Int = 0, offsetY: Int = 0, similarity: Double = DEFAULT_SIMILARITY) : this(
        listOf(ImageElementDefinition(imagePath)), offsetX, offsetY, similarity
    )

    override fun findElement(context: SearchContext?): ImageWebElement {
        val screenshotStream = (context as TakesScreenshot).getScreenshotAs(OutputType.BYTES).inputStream()
        val screenshot = ImageIO.read(screenshotStream)
        val finder = Finder(screenshot)

        imageElementDefinitions.forEach {
            val pattern = Pattern(it.path.toString()).similar(similarity).targetOffset(offsetX, offsetY)
            if (finder.find(pattern) == null) {
                throw RuntimeException("Find setup for image $it with similarity $similarity is not possible in context $context")
            } else if (finder.hasNext()) {
                val match = finder.next()
                logger.debug { "Match: (${match.x}, ${match.y}. Match score: ${match.score}" }
                return ImageWebElement(match, context, it.enabled, it.selected)
            }
        }
        // Debug
        ImageIO.write(screenshot, "png", File("build/tmp/screenshot.png"))
        throw NoSuchElementException("Image $imageElementDefinitions not found in context $context")
    }

    override fun findElements(context: SearchContext?): List<ImageWebElement> {
        val screenshotStream = (context as TakesScreenshot).getScreenshotAs(OutputType.BYTES).inputStream()
        val screenshot = ImageIO.read(screenshotStream)
        val finder = Finder(screenshot)
        val elements = mutableListOf<ImageWebElement>()
        imageElementDefinitions.forEach {
            val pattern = Pattern(it.path.toString()).similar(similarity).targetOffset(offsetX, offsetY)
            if (finder.find(pattern) == null) {
                throw RuntimeException("Find setup for image $it with similarity $similarity is not possible in context $context")
            } else {
                elements.addAll(finder.list.map { match -> ImageWebElement(match, context, it.enabled, it.selected) })
            }
        }
        return elements
    }

    companion object {
        private const val DEFAULT_SIMILARITY = 0.7

        fun name(value: String, offsetX: Int = 0, offsetY: Int = 0, similarity: Double = DEFAULT_SIMILARITY): ByImage {
            val folder = Thread.currentThread().contextClassLoader.getResource("images/$value")!!.toURI()!!.toPath()
            assertTrue("$folder should be a directory") { folder.isDirectory() }
            val files = folder.listDirectoryEntries()
            assertTrue("$folder should not contain directories") { files.all { !it.isDirectory() } }
            val definitions = files.filter { it.isRegularFile() }.map {
                val enabled = !it.name.contains("disabled", ignoreCase = true)
                val selected = it.name.contains("selected", ignoreCase = true)
                ImageElementDefinition(it, enabled, selected)
            }
            return ByImage(definitions, offsetX, offsetY, similarity)
        }
    }
}
