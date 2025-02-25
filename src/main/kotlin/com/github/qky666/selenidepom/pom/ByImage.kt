package com.github.qky666.selenidepom.pom

import com.github.qky666.selenidepom.condition.ImageElementDefinition
import com.github.qky666.selenidepom.data.ResourceHelper.Companion.getResourcePath
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * A [By] subclass tha uses images (a list of [ImageElementDefinition]) to locate web elements in a web page.
 *
 * @param imageElementDefinitions list of images ([ImageElementDefinition]) used to locate the element
 * @param offsetX x-axis offset from the center (in pixels) used for click operations in found web elements. Default value: 0
 * @param offsetY y-axis offset from the center (in pixels) used for click operations in found web elements. Default value: 0
 * @param similarity threshold used to check if is considered that one image is contained in the web page. Default value: [DEFAULT_SIMILARITY]
 * @constructor created [By] instance
 */
class ByImage(
    private val imageElementDefinitions: List<ImageElementDefinition>,
    private val offsetX: Int = 0,
    private val offsetY: Int = 0,
    private val similarity: Double = DEFAULT_SIMILARITY,
) : By() {
    private val logger = KotlinLogging.logger {}

    /**
     * Same as default constructor, but uses a single image [Path] instead of a list of [ImageElementDefinition].
     *
     * @param imagePath the [Path] of the image used to search in web page
     * @param offsetX x-axis offset from the center (in pixels) used for click operations in found web elements. Default value: 0
     * @param offsetY y-axis offset from the center (in pixels) used for click operations in found web elements. Default value: 0
     * @param similarity threshold used to check if is considered that one image is contained in the web page. Default value: [DEFAULT_SIMILARITY]
     */
    constructor(imagePath: Path, offsetX: Int = 0, offsetY: Int = 0, similarity: Double = DEFAULT_SIMILARITY) : this(
        listOf(ImageElementDefinition(imagePath)), offsetX, offsetY, similarity
    )

    /**
     * Same as default constructor, but uses a single image path ([String]) instead of a list of [ImageElementDefinition].
     *
     * @param imagePath the path ([String]) of the image used to search in web page
     * @param offsetX x-axis offset from the center (in pixels) used for click operations in found web elements. Default value: 0
     * @param offsetY y-axis offset from the center (in pixels) used for click operations in found web elements. Default value: 0
     * @param similarity threshold used to check if is considered that one image is contained in the web page. Default value: [DEFAULT_SIMILARITY]
     */
    constructor(imagePath: String, offsetX: Int = 0, offsetY: Int = 0, similarity: Double = DEFAULT_SIMILARITY) : this(
        Path.of(imagePath), offsetX, offsetY, similarity
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
        const val DEFAULT_SIMILARITY = 0.7

        /**
         * Utility function used to create a [ByImage] instance using the files in a `resources/images` subfolder.
         * All files in [value] folder are used to create a list of [ImageElementDefinition],
         * and the [ByImage] instance is created using them.
         * If a file contains `disabled` in its name, the corresponding [ImageElementDefinition]
         * is created with [ImageElementDefinition.enabled] property as `false`.
         * If a file contains `selected` in its name, the corresponding [ImageElementDefinition]
         * is created with [ImageElementDefinition.selected] property as `true`.
         * Otherwise, the [ImageElementDefinition] are created with [ImageElementDefinition.enabled] property as `true`
         * and [ImageElementDefinition.selected] property as `false`.
         *
         * @param value relative path to the folder in `resources/images`
         * @param offsetX x-axis offset from the center (in pixels) used for click operations in found web elements. Default value: 0
         * @param offsetY y-axis offset from the center (in pixels) used for click operations in found web elements. Default value: 0
         * @param similarity threshold used to check if is considered that one image is contained in the web page. Default value: [DEFAULT_SIMILARITY]
         * @return the [ByImage] instance created
         */
        fun name(value: String, offsetX: Int = 0, offsetY: Int = 0, similarity: Double = DEFAULT_SIMILARITY): ByImage {
            val folder = getResourcePath("images/$value")
            assertNotNull(folder, "Resource 'images/$value' does not exist")
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
