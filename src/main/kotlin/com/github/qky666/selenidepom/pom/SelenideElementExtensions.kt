package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.config.SPConfig
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.global.opencv_imgproc
import org.bytedeco.tesseract.TessBaseAPI


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
 * @return `this`
 */
fun <T : SelenideElement> T.clickImage(clickOption: ClickOptions): T {
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
 * For possible [language] values, see [tesseract documentation](https://tesseract-ocr.github.io/tessdoc/Data-Files-in-different-versions.html)
 *
 * @param language the language used by `tesseract` to extract the text
 * @param tessdata directory where `tesseract` data files are located
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
        val extractedText = tesseract.GetUTF8Text().string
        tesseract.End()
        return extractedText
    }
    return ""
}
