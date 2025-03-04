package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.ocrText
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * Some functions that create [WebElementCondition] related to element's OCR text
 * (text obtained from element's screenshot using OCR).
 */
object OcrTextCondition {
    private val logger = KotlinLogging.logger {}

    /**
     * Creates a [WebElementCondition] that checks if a web element screenshot contains a given [text] using OCR.
     *
     * For possible [language] values,
     * see [tesseract documentation](https://tesseract-ocr.github.io/tessdoc/Data-Files-in-different-versions.html)
     *
     * @param text the text to find
     * @param ignoreCase if [text] case should be ignored or not. Default value: true
     * @param language language used for OCR text recognition. Default value: [SPConfig.lang]
     * @param tessdata directory where `tesseract` data files are located. Default value: System.getenv("TESSDATA_PREFIX")
     * @return the [WebElementCondition] that checks if a web element screenshot contains given [text] (using OCR)
     */
    fun ocrText(
        text: String,
        ignoreCase: Boolean = true,
        language: String = SPConfig.lang,
        tessdata: String = System.getenv("TESSDATA_PREFIX"),
    ): WebElementCondition {
        return Condition.match("Web element screenshot text (obtained using OCR) contains '$text'") {
            val extractedText = Selenide.element(it).ocrText(language, tessdata)
            logger.info { "Element extracted text '$extractedText'. Searched text: '$text'" }
            return@match extractedText.contains(text, ignoreCase)
        }
    }

    /**
     * Creates a [WebElementCondition] that checks if a web element screenshot contains exactly
     * (without any additional text) a given [text] using OCR.
     *
     * For possible [language] values,
     * see [tesseract documentation](https://tesseract-ocr.github.io/tessdoc/Data-Files-in-different-versions.html)
     *
     * @param text the text to find
     * @param ignoreCase if [text] case should be ignored or not. Default value: true
     * @param language language used for OCR text recognition. Default value: [SPConfig.lang]
     * @param tessdata directory where `tesseract` data files are located. Default value: System.getenv("TESSDATA_PREFIX")
     * @return the [WebElementCondition] that checks if a web element screenshot contains exactly given [text] (using OCR)
     */
    fun exactOcrText(
        text: String,
        ignoreCase: Boolean = true,
        language: String = SPConfig.lang,
        tessdata: String = System.getenv("TESSDATA_PREFIX"),
    ): WebElementCondition {
        return Condition.match("Web element screenshot text (obtained using OCR) is '$text'") {
            val extractedText = Selenide.element(it).ocrText(language, tessdata)
            logger.info { "Element extracted text '$extractedText'. Exact searched text: '$text'" }
            return@match extractedText.contentEquals(text, ignoreCase)
        }
    }
}
