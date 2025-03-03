package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.Condition
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.condition.OcrTextConstants.DEFAULT_OCR_LANGUAGE
import com.github.qky666.selenidepom.pom.ByImage.Companion.DEFAULT_SIMILARITY
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.global.opencv_imgproc
import org.bytedeco.tesseract.TessBaseAPI
import org.openqa.selenium.OutputType

object OcrTextConstants {
    const val DEFAULT_OCR_LANGUAGE = "eng"
}

fun ocrText(
    text: String,
    ignoreCase: Boolean = true,
    language: String = DEFAULT_OCR_LANGUAGE,
): WebElementCondition {
    return Condition.match("ocrText") {
        val image = imread(it.getScreenshotAs(OutputType.FILE).absolutePath, opencv_imgcodecs.IMREAD_GRAYSCALE)
        opencv_imgproc.threshold(image, image, 0.0, 255.0, opencv_imgproc.THRESH_BINARY or opencv_imgproc.THRESH_OTSU)
        val tesseract = TessBaseAPI()
        val tessdata = System.getenv("TESSDATA_PREFIX")
        tesseract.Init(tessdata, language)
        tesseract.SetImage(image.data(), image.cols(), image.rows(), image.channels(), image.step1().toInt())
        val extractedText = tesseract.GetUTF8Text().string
        tesseract.End()
        return@match extractedText.contentEquals(text, ignoreCase)
    }
}

fun exactOcrText(
    text: String,
    ignoreCase: Boolean = true,
    language: String = DEFAULT_OCR_LANGUAGE,
): WebElementCondition {
    return Condition.match("ocrText") {
        val image = imread(it.getScreenshotAs(OutputType.FILE).absolutePath, opencv_imgcodecs.IMREAD_GRAYSCALE)
        opencv_imgproc.threshold(image, image, 0.0, 255.0, opencv_imgproc.THRESH_BINARY or opencv_imgproc.THRESH_OTSU)
        val tesseract = TessBaseAPI()
        val tessdata = System.getenv("TESSDATA_PREFIX")
        tesseract.Init(tessdata, language)
        tesseract.SetImage(image.data(), image.cols(), image.rows(), image.channels(), image.step1().toInt())
        val extractedText = tesseract.GetUTF8Text().string
        tesseract.End()
        return@match extractedText.equals(text, ignoreCase)
    }
}
