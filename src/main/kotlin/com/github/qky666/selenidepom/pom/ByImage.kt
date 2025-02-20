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
    private val imageEnabledPaths: List<Path>,
    private val imageDisabledPaths: List<Path> = listOf(),
    private val imageSelectedPaths: List<Path> = listOf(),
    private val offsetX: Int = 0,
    private val offsetY: Int = 0,
    private val similarity: Double = 0.7,
) : By() {
    private val logger = KotlinLogging.logger {}

    constructor(imageEnabledPath: Path, offsetX: Int = 0, offsetY: Int = 0, similarity: Double = 0.7) : this(
        listOf(imageEnabledPath), listOf(), listOf(), offsetX, offsetY, similarity
    )

    override fun findElement(context: SearchContext?): ImageWebElement {
        val screenshotStream = (context as TakesScreenshot).getScreenshotAs(OutputType.BYTES).inputStream()
        val screenshot = ImageIO.read(screenshotStream)
        val finder = Finder(screenshot)

        imageEnabledPaths.forEach {
            val pattern = Pattern(it.toString()).similar(similarity).targetOffset(offsetX, offsetY)
            if (finder.find(pattern) == null) {
                throw RuntimeException("Find setup for image $it with similarity $similarity is not possible in context $context")
            } else if (finder.hasNext()) {
                val match = finder.next()
                logger.debug { "Match: (${match.x}, ${match.y}. Match score: ${match.score}" }
                return ImageWebElement(match, context)
            }
        }
        imageDisabledPaths.forEach {
            val pattern = Pattern(it.toString()).similar(similarity).targetOffset(offsetX, offsetY)
            if (finder.find(pattern) == null) {
                throw RuntimeException("Find setup for image $it with similarity $similarity is not possible in context $context")
            } else if (finder.hasNext()) {
                val match = finder.next()
                logger.debug { "Match: (${match.x}, ${match.y}. Match score: ${match.score}" }
                return ImageWebElement(match, context, enabled = false)
            }
        }
        imageSelectedPaths.forEach {
            val pattern = Pattern(
                it.toString()
            ).similar(similarity).targetOffset(offsetX, offsetY)
            if (finder.find(pattern) == null) {
                throw RuntimeException("Find setup for image $it with similarity $similarity is not possible in context $context")
            } else if (finder.hasNext()) {
                val match = finder.next()
                logger.debug { "Match: (${match.x}, ${match.y}. Match score: ${match.score}" }
                return ImageWebElement(match, context, selected = true)
            }
        }
        // Debug
        ImageIO.write(screenshot, "png", File("build/tmp/screenshot.png"))
        throw NoSuchElementException("Image $imageEnabledPaths, $imageDisabledPaths, $imageSelectedPaths not found in context $context")
    }

    override fun findElements(context: SearchContext?): List<ImageWebElement> {
        val screenshotStream = (context as TakesScreenshot).getScreenshotAs(OutputType.BYTES).inputStream()
        val screenshot = ImageIO.read(screenshotStream)
        val finder = Finder(screenshot)
        val elements = mutableListOf<ImageWebElement>()
        imageEnabledPaths.forEach {
            val pattern = Pattern(it.toString()).similar(similarity).targetOffset(offsetX, offsetY)
            if (finder.find(pattern) == null) {
                throw RuntimeException("Find setup for image $it with similarity $similarity is not possible in context $context")
            } else {
                elements.addAll(finder.list.map { ImageWebElement(it, context) })
            }
        }
        imageDisabledPaths.forEach {
            val pattern = Pattern(it.toString()).similar(similarity).targetOffset(offsetX, offsetY)
            if (finder.find(pattern) == null) {
                throw RuntimeException("Find setup for image $it with similarity $similarity is not possible in context $context")
            } else {
                elements.addAll(finder.list.map { ImageWebElement(it, context, enabled = false) })
            }
        }
        imageSelectedPaths.forEach {
            val pattern = Pattern(it.toString()).similar(similarity).targetOffset(offsetX, offsetY)
            if (finder.find(pattern) == null) {
                throw RuntimeException("Find setup for image $it with similarity $similarity is not possible in context $context")
            } else {
                elements.addAll(finder.list.map { ImageWebElement(it, context, selected = true) })
            }
        }
        return elements
    }

    companion object {
        fun name(value: String, offsetX: Int = 0, offsetY: Int = 0, similarity: Double = 0.7): ByImage {
            val folder = Thread.currentThread().contextClassLoader.getResource("images/$value")!!.toURI()!!.toPath()
            assertTrue("$folder should be a directory") { folder.isDirectory() }
            val files = folder.listDirectoryEntries()
            assertTrue("$folder should not contain directories") { files.all { !it.isDirectory() } }
            val enabled = mutableListOf<Path>()
            val disabled = mutableListOf<Path>()
            val selected = mutableListOf<Path>()
            files.filter { it.isRegularFile() }.forEach {
                if (it.name.contains("selected", ignoreCase = true)) {
                    selected.add(it)
                } else if (it.name.contains("disabled", ignoreCase = true)) {
                    disabled.add(it)
                } else {
                    enabled.add(it)
                }
            }
            return ByImage(enabled, disabled, selected, offsetX, offsetY, similarity)
        }
    }
}
