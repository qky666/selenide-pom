package com.github.qky666.selenidepom.pom

import com.github.qky666.selenidepom.data.ResourceHelper.Companion.getResourcePath
import com.github.qky666.selenidepom.pom.ByImage.Companion.DEFAULT_SIMILARITY
import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.opencv.global.opencv_core.CV_32FC1
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.global.opencv_imgproc.TM_CCOEFF_NORMED
import org.bytedeco.opencv.global.opencv_imgproc.matchTemplate
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.Rect
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.Rectangle
import org.openqa.selenium.SearchContext
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebElement
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
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
 * @param similarity threshold used to check if is considered that one image is contained in the web page. Default value: [DEFAULT_SIMILARITY]
 * @constructor created [By] instance
 */
class ByImage(
    private val imageElementDefinitions: List<ImageElementDefinition>,
    private val similarity: Double = DEFAULT_SIMILARITY,
) : By() {

    /**
     * Same as default constructor, but uses a single image [Path] instead of a list of [ImageElementDefinition].
     *
     * @param imagePath the [Path] of the image used to search in web page
     * @param similarity threshold used to check if is considered that one image is contained in the web page. Default value: [DEFAULT_SIMILARITY]
     */
    constructor(imagePath: Path, similarity: Double = DEFAULT_SIMILARITY) : this(
        listOf(ImageElementDefinition(imagePath)), similarity
    )

    /**
     * Same as default constructor, but uses a single image path ([String]) instead of a list of [ImageElementDefinition].
     *
     * @param imagePath the path ([String]) of the image used to search in web page
     * @param similarity threshold used to check if is considered that one image is contained in the web page. Default value: [DEFAULT_SIMILARITY]
     */
    constructor(imagePath: String, similarity: Double = DEFAULT_SIMILARITY) : this(Path.of(imagePath), similarity)

    private val logger = KotlinLogging.logger {}

    private fun smallestContainer(context: SearchContext, rect: Rectangle): WebElement {
        val elements = context.findElements(xpath(".//*"))
        var container = context as? WebElement ?: context.findElement(xpath("./*"))
        elements.forEach { element ->
            val elementRect = element.rect
            if (elementRect.isContainedIn(container.rect) && elementRect.contains(rect)) {
                container = element
            }
        }
        logger.debug { "Found smallest container for Rectangle 'x: ${rect.x}, y: ${rect.y}, width: ${rect.width}, height: ${rect.height}': $container" }
        return container
    }

    private fun rectInPage(context: SearchContext, rect: Rect): Rectangle {
        val rectPage = if (context is WebElement) {
            Rectangle(context.location.x + rect.x(), context.location.y + rect.y(), rect.height(), rect.width())
        } else {
            Rectangle(rect.x(), rect.y(), rect.height(), rect.width())
        }
        logger.debug {
            "Rectangle in page found for Rect 'x: ${rect.x()}, y: ${rect.y()}, width: ${rect.width()}, height: ${rect.height()}' in context '${context}': 'x: ${rectPage.x}, y: ${rectPage.y}, width: ${rectPage.width}, height: ${rectPage.height}"
        }
        return rectPage
    }

    override fun findElement(context: SearchContext): ImageElement {
        val screenshotFile = (context as TakesScreenshot).getScreenshotAs(OutputType.FILE)
        val screenshot = imread(screenshotFile.absolutePath)

        imageElementDefinitions.forEach {
            val pattern = imread(it.path.toString())
            val result = Mat(screenshot.cols() - pattern.cols() + 1, screenshot.rows() - pattern.rows() + 1, CV_32FC1)
            matchTemplate(screenshot, pattern, result, TM_CCOEFF_NORMED)
            val matchPoint = getFirstPointFromMatAboveThreshold(result, similarity.toFloat())
            matchPoint?.let { mp ->
                val matchSize = pattern.size()
                logger.debug { "Match in findElement: (${mp.x()}, ${mp.y()}). Size: width: ${matchSize.width()}, height: ${matchSize.height()}" }
                val matchRect = rectInPage(context, Rect(mp, matchSize))
                val container = smallestContainer(context, matchRect)
                return ImageElement(container, matchRect, it.enabled, it.selected)
            }
        }
        // Debug if image not found
        val path = screenshotFile.toPath()
        val name = path.name
        val copied = Files.copy(path, File("build/tmp").toPath().resolve(name), StandardCopyOption.REPLACE_EXISTING)
        throw NoSuchElementException("Image $imageElementDefinitions not found in context $context. Screenshot saved to $copied")
    }

    override fun findElements(context: SearchContext): List<ImageElement> {
        val screenshotFile = (context as TakesScreenshot).getScreenshotAs(OutputType.FILE)
        val screenshot = imread(screenshotFile.absolutePath)
        val elements = mutableListOf<ImageElement>()
        imageElementDefinitions.forEach {
            val pattern = imread(it.path.toString())
            val result = Mat(screenshot.cols() - pattern.cols() + 1, screenshot.rows() - pattern.rows() + 1, CV_32FC1)
            matchTemplate(screenshot, pattern, result, TM_CCOEFF_NORMED)
            val matchPoints = getPointsFromMatAboveThreshold(result, similarity.toFloat())
            elements.addAll(matchPoints.map { mp ->
                val matchSize = pattern.size()
                logger.debug { "Match in findElements: (${mp.x()}, ${mp.y()}. Size: width: ${matchSize.width()}, height: ${matchSize.height()}" }
                val matchRect = rectInPage(context, Rect(mp, matchSize))
                val container = smallestContainer(context, matchRect)
                ImageElement(container, matchRect, it.enabled, it.selected)
            })
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
         * @param similarity threshold used to check if is considered that one image is contained in the web page. Default value: [DEFAULT_SIMILARITY]
         * @return the [ByImage] instance created
         */
        fun name(value: String, similarity: Double = DEFAULT_SIMILARITY): ByImage {
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
            return ByImage(definitions, similarity)
        }
    }
}
