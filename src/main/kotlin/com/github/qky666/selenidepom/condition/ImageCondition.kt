package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.Condition
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.pom.ByImage.Companion.DEFAULT_SIMILARITY
import org.openqa.selenium.OutputType
import org.sikuli.script.Finder
import org.sikuli.script.Pattern
import java.nio.file.Path
import javax.imageio.ImageIO

class ImageCondition {
    companion object {
        fun imageContent(images: List<String>, similarity: Double = DEFAULT_SIMILARITY): WebElementCondition {
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

        fun imageContent(img: String, similarity: Double = DEFAULT_SIMILARITY): WebElementCondition {
            return imageContent(listOf(img), similarity)
        }

        fun imageContent(img: Path, similarity: Double = DEFAULT_SIMILARITY): WebElementCondition {
            return imageContent(listOf(img.toString()), similarity)
        }
    }
}
