package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.condition.OcrTextCondition
import com.github.qky666.selenidepom.config.SPConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.javacpp.PointerPointer
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.global.opencv_imgproc
import org.bytedeco.tesseract.TessBaseAPI
import kotlin.test.assertTrue

private val logger = KotlinLogging.logger {}

/**
 * Same as [SelenideElement.scrollIntoView] ("{behavior: \"auto\", block: \"center\", inline: \"center\"}").
 * See also [SelenideElement.scrollIntoCenter].
 *
 * @return `this`
 */
fun <T : SelenideElement> T.scrollToCenter(): T {
    this.scrollIntoView("{behavior: \"auto\", block: \"center\", inline: \"center\"}")
    return this
}

/**
 * If `this` is an [ImageElement], click is performed correcting [clickOption] offset to use center of image as reference.
 * If not, usual [SelenideElement.click] is performed.
 *
 * @param clickOption the [ClickOptions] used. Default value: `ClickOptions.usingDefaultMethod()`
 * @return `this`
 */
fun <T : SelenideElement> T.clickImage(clickOption: ClickOptions = ClickOptions.usingDefaultMethod()): T {
    val imageElement = this.toWebElement() as? ImageElement
    if (imageElement != null) {
        imageElement.clickImage(clickOption)
    } else {
        this.click(clickOption)
    }
    return this
}

/**
 * Returns the text contained in `this` [SelenideElement] using OCR to get it.
 *
 * For possible [language] values,
 * see [tesseract documentation](https://tesseract-ocr.github.io/tessdoc/Data-Files-in-different-versions.html)
 *
 * @param language language used for OCR text recognition. Default value: [SPConfig.lang]
 * @param tessdata directory where `tesseract` data files are located. Default value: System.getenv("TESSDATA_PREFIX")
 * @return the extracted text
 */
fun <T : SelenideElement> T.ocrText(
    language: String = SPConfig.lang,
    tessdata: String = System.getenv("TESSDATA_PREFIX"),
): String {
    screenshot()?.let {
        val image = imread(it.absolutePath, opencv_imgcodecs.IMREAD_GRAYSCALE)
        opencv_imgproc.threshold(image, image, 0.0, 255.0, opencv_imgproc.THRESH_BINARY or opencv_imgproc.THRESH_OTSU)
        val tesseract = TessBaseAPI()
        tesseract.Init(tessdata, language)
        tesseract.SetImage(image.data(), image.cols(), image.rows(), image.channels(), image.step1().toInt())
        val extractedText = tesseract.GetUTF8Text().string.trim()
        tesseract.End()
        return extractedText
    }
    return ""
}

/**
 * Clicks on a [text] located using OCR in this [SelenideElement] screenshot.
 *
 * @param text the text to find
 * @param wordIndexToClick the word number (zero based) inside [text] where click is performed. Default value: 0
 * @param ignoreCase if [text] case should be ignored or not. Default value: true
 * @param clickOption the [ClickOptions] used. Default value: `ClickOptions.usingDefaultMethod()`
 * @param language language used for OCR text recognition. Default value: [SPConfig.lang]
 * @param tessdata directory where `tesseract` data files are located. Default value: System.getenv("TESSDATA_PREFIX")
 * @return this
 */
fun <T : SelenideElement> T.clickOcrText(
    text: String,
    wordIndexToClick: Int = 0,
    ignoreCase: Boolean = true,
    clickOption: ClickOptions = ClickOptions.usingDefaultMethod(),
    language: String = SPConfig.lang,
    tessdata: String = System.getenv("TESSDATA_PREFIX"),
): T {
    this.shouldHave(OcrTextCondition.ocrText(text, ignoreCase, language, tessdata))
    val words = text.split(" ")
    val wordIndex = when {
        wordIndexToClick < 0 -> 0
        wordIndexToClick >= words.size -> words.size - 1
        else -> wordIndexToClick
    }
    screenshot()?.let {
        val image = imread(it.absolutePath, opencv_imgcodecs.IMREAD_GRAYSCALE)
        opencv_imgproc.threshold(image, image, 0.0, 255.0, opencv_imgproc.THRESH_BINARY or opencv_imgproc.THRESH_OTSU)
        val tesseract = TessBaseAPI()
        tesseract.Init(tessdata, language)
        tesseract.SetImage(image.data(), image.cols(), image.rows(), image.channels(), image.step1().toInt())
//        val boxes = tesseract.GetComponentImages(RIL_WORD, true, null as PointerPointer<*>?, null)
        val boxes = tesseract.GetWords(null as PointerPointer<*>?)
        var match: Boolean
        var clicked = false
        for (i in 0 until boxes.n()) {
            val box = boxes.box(i)
            tesseract.SetRectangle(box.x(), box.y(), box.w(), box.h())
            val extractedText = tesseract.GetUTF8Text().string.trim()
            if (extractedText.equals(words[0], ignoreCase)) {
                logger.info { "Current box extracted text match first searched word: $extractedText" }
                match = true
                for (j in 1 until words.size) {
                    if (i + j >= boxes.n()) {
                        logger.info { "Not enough boxes detected. Will start search again from first word" }
                        match = false
                        break
                    }
                    val nextBox = boxes.box(i + j)
                    tesseract.SetRectangle(nextBox.x(), nextBox.y(), nextBox.w(), nextBox.h())
                    val nextText = tesseract.GetUTF8Text().string.trim()
                    if (!nextText.equals(words[j], ignoreCase)) {
                        logger.info { "Next box text should be '${words[j]}' but it is $nextText. Will start search again from first word" }
                        match = false
                        break
                    }
                }
                if (match) {
                    val elementCenterX = size.width / 2
                    val elementCenterY = size.height / 2
                    val boxWordIndex = boxes.box(wordIndex)
                    val boxCenterX = boxWordIndex.x() + boxWordIndex.w() / 2
                    val boxCenterY = boxWordIndex.y() + boxWordIndex.h() / 2
                    val option =
                        correctClickOptionOffset(clickOption, boxCenterX - elementCenterX, boxCenterY - elementCenterY)
                    logger.info { "Trying to click ocr detected text '$text' using element at location (${location.x}, ${location.y}) with element center (from this location) ($elementCenterX, $elementCenterY) and offset (from element center) (${option.offsetX()}, ${option.offsetY()})" }
                    click(option)
                    clicked = true
                    break
                }
            }
        }
        tesseract.End()
        assertTrue(clicked, "Click has not been performed in clickOcrText")
    }
    return this
}
