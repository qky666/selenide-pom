package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.condition.OcrTextCondition
import com.github.qky666.selenidepom.config.SPConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import net.sourceforge.tess4j.ITessAPI
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode
import net.sourceforge.tess4j.Tesseract
import kotlin.test.assertTrue

private val logger = KotlinLogging.logger {}

/**
 * If `this` is an [ImageElement], click is performed correcting [clickOption] offset to use center of image as reference.
 * If not, usual [SelenideElement.click] is performed.
 *
 * @param clickOption the [ClickOptions] used. Default value: `ClickOptions.usingDefaultMethod()`
 * @return `this`
 */
fun <T : SelenideElement> T.clickImage(clickOption: ClickOptions = ClickOptions.usingDefaultMethod()): T {
    val imageElement = this.toWebElement() as? ImageElement
    imageElement?.clickImage(clickOption) ?: this.click(clickOption)
    return this
}

/**
 * Returns the text contained in `this` [SelenideElement] using OCR to get it.
 *
 * For possible [language] values,
 * see [tesseract documentation](https://tesseract-ocr.github.io/tessdoc/Data-Files-in-different-versions.html)
 *
 * @param language language used for OCR text recognition. Default value: [SPConfig.lang]
 * @param tessPageSegMode The [TessPageSegMode] to use in OCR operation
 * @param tessdata directory where `tesseract` data files are located. Default value: `null` (internally uses TESSDATA_PREFIX environment variable)
 * @return the extracted text
 */
fun <T : SelenideElement> T.ocrText(
    language: String = SPConfig.lang,
    tessPageSegMode: Int = TessPageSegMode.PSM_SPARSE_TEXT_OSD,
    tessdata: String? = null,
) = this.screenshotAsImage()?.let { screenshot ->
    val tesseract = Tesseract()
    tessdata?.let { tesseract.setDatapath(it) }
    tesseract.setLanguage(language)
    tesseract.setPageSegMode(tessPageSegMode)
    tesseract.doOCR(screenshot).trim()
} ?: ""

/**
 * Clicks on a [text] located using OCR in this [SelenideElement] screenshot.
 *
 * @param text the text to find
 * @param wordIndexToClick the word number (zero-based) inside [text] where click is performed. Default value: 0
 * @param ignoreCase if [text] case should be ignored or not. Default value: true
 * @param clickOption the [ClickOptions] used. Default value: `ClickOptions.usingDefaultMethod()`
 * @param language language used for OCR text recognition. Default value: [SPConfig.lang]
 * @param tessPageSegMode The [TessPageSegMode] to use in OCR operation
 * @param tessdata directory where `tesseract` data files are located. Default value: `null` (internally uses TESSDATA_PREFIX environment variable)
 * @return `this`
 */
fun <T : SelenideElement> T.clickOcrText(
    text: String,
    wordIndexToClick: Int = 0,
    ignoreCase: Boolean = true,
    clickOption: ClickOptions = ClickOptions.usingDefaultMethod(),
    language: String = SPConfig.lang,
    tessPageSegMode: Int = TessPageSegMode.PSM_SPARSE_TEXT_OSD,
    tessdata: String? = null,
): T {
    this.shouldHave(OcrTextCondition.ocrText(text, ignoreCase, language, tessPageSegMode, tessdata))
    val textWords = text.split(" ")
    val wordIndex = when {
        wordIndexToClick < 0 -> 0
        wordIndexToClick >= textWords.size -> textWords.size - 1
        else -> wordIndexToClick
    }
    this.screenshotAsImage()?.let { screenshot ->
        val tesseract = Tesseract()
        tessdata?.let { tesseract.setDatapath(it) }
        tesseract.setLanguage(language)
        tesseract.setPageSegMode(tessPageSegMode)
        val words = tesseract.getWords(screenshot, ITessAPI.TessPageIteratorLevel.RIL_WORD)
        val fullTextExtracted = (words.map { it.text }).joinToString(" ")
        assertTrue("Full text extracted: \n'$fullTextExtracted'\nshould contain: '$text'") {
            fullTextExtracted.contains(text, ignoreCase)
        }
        var clicked = false
        for ((i, word) in words.withIndex()) {
            val extractedText = word.text.trim()
            if (extractedText.equals(textWords[0], ignoreCase)) {
                logger.debug { "Current extracted word match first searched word: $extractedText" }
                var match = true
                for (j in 1 until textWords.size) {
                    if (i + j >= words.size) {
                        logger.debug { "Not enough words detected. Will start search again from first word" }
                        match = false
                        break
                    }
                    val nextWord = words[i + j]
                    val nextText = nextWord.text.trim()
                    if (!nextText.equals(textWords[j], ignoreCase)) {
                        logger.debug { "Next box text should be '${textWords[j]}' but it is $nextText. Will start search again from first word" }
                        match = false
                        break
                    }
                }
                if (match) {
                    val elementCenterX = size.width / 2
                    val elementCenterY = size.height / 2
                    val boxWordIndex = words[i + wordIndex].boundingBox
                    val boxCenterX = boxWordIndex.x + boxWordIndex.width / 2
                    val boxCenterY = boxWordIndex.y + boxWordIndex.height / 2
                    val option =
                        correctClickOptionOffset(clickOption, boxCenterX - elementCenterX, boxCenterY - elementCenterY)
                    logger.info { "Trying to click ocr detected text '$text' using element at location (${location.x}, ${location.y}) with element center (from this location) ($elementCenterX, $elementCenterY) and offset (from element center) (${option.offsetX()}, ${option.offsetY()})" }
                    click(option)
                    clicked = true
                    break
                }
            }
        }
        assertTrue(clicked, "Click has not been performed in clickOcrText")
    }
    return this
}
