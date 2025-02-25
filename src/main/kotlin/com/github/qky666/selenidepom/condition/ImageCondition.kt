package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.Condition
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.pom.ByImage.Companion.DEFAULT_SIMILARITY
import org.openqa.selenium.OutputType
import org.sikuli.script.Finder
import org.sikuli.script.Pattern
import java.nio.file.Path
import javax.imageio.ImageIO

/**
 * Creates a [WebElementCondition] that checks if a web element screenshot contains any of a list of predefined images.
 *
 * @param images list of image paths used to check if the web element screenshot contains any of them
 * @param similarity threshold used to check if is considered that one image is contained in the screenshot. Default value: [DEFAULT_SIMILARITY]
 * @return the [WebElementCondition] that checks if a web element screenshot contains any of the images
 */
fun containsImage(images: List<String>, similarity: Double = DEFAULT_SIMILARITY): WebElementCondition {
    return Condition.match("image") { webElement ->
        val screenshotStream = webElement.getScreenshotAs(OutputType.BYTES).inputStream()
        val screenshot = ImageIO.read(screenshotStream)
        val finder = Finder(screenshot)
        images.forEach {
            val pattern = Pattern(it).similar(similarity)
            if (finder.find(pattern) == null) {
                throw RuntimeException("Find setup for image $it with similarity $similarity is not possible in webElement $webElement")
            } else if (finder.hasNext()) {
                return@match true
            }
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
