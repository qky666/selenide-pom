package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.Condition
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.pom.ByImage.Companion.DEFAULT_SIMILARITY
import com.github.qky666.selenidepom.pom.getFirstPointFromMatAboveThreshold
import org.bytedeco.opencv.global.opencv_core.CV_32FC1
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.global.opencv_imgproc.TM_CCOEFF_NORMED
import org.bytedeco.opencv.global.opencv_imgproc.matchTemplate
import org.bytedeco.opencv.opencv_core.Mat
import org.openqa.selenium.OutputType
import java.nio.file.Path


/**
 * Some functions that create [WebElementCondition] related to element's image (screenshot).
 */
@Suppress("unused")
object ImageCondition {
    /**
     * Creates a [WebElementCondition] that checks if a web element screenshot contains any of a list of predefined images.
     *
     * @param images list of image paths used to check if the web element screenshot contains any of them
     * @param similarity threshold used to check if is considered that one image is contained in the screenshot. Default value: [DEFAULT_SIMILARITY]
     * @return the [WebElementCondition] that checks if a web element screenshot contains any of the images
     */
    fun containsImage(images: List<String>, similarity: Double = DEFAULT_SIMILARITY): WebElementCondition {
        return Condition.match("Web element image (screenshot) contains one of theses images: $images") { webElement ->
            val screenshotFile = webElement.getScreenshotAs(OutputType.FILE)
            val screenshot = imread(screenshotFile.absolutePath)

            images.forEach {
                val pattern = imread(it)
                val result =
                    Mat(screenshot.cols() - pattern.cols() + 1, screenshot.rows() - pattern.rows() + 1, CV_32FC1)
                matchTemplate(screenshot, pattern, result, TM_CCOEFF_NORMED)
                getFirstPointFromMatAboveThreshold(result, similarity.toFloat())?.let { return@match true }
            }
            return@match false
        }
    }

    /**
     * Creates a [WebElementCondition] that checks if a web element screenshot contains a predefined image.
     *
     * @param img path of the image used to check if the web element screenshot contains it
     * @param similarity threshold used to check if is considered that the image is contained in the screenshot. Default value: [DEFAULT_SIMILARITY]
     * @return the [WebElementCondition] that checks if a web element screenshot contains the image
     */
    fun containsImage(img: String, similarity: Double = DEFAULT_SIMILARITY): WebElementCondition {
        return containsImage(listOf(img), similarity)
    }

    /**
     * Creates a [WebElementCondition] that checks if a web element screenshot contains a predefined image.
     *
     * @param img [Path] of the image used to check if the web element screenshot contains it
     * @param similarity threshold used to check if is considered that the image is contained in the screenshot. Default value: [DEFAULT_SIMILARITY]
     * @return the [WebElementCondition] that checks if a web element screenshot contains the image
     */
    fun containsImage(img: Path, similarity: Double = DEFAULT_SIMILARITY): WebElementCondition {
        return containsImage(listOf(img.toString()), similarity)
    }
}
