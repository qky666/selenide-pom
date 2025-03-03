package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.ocrText

fun ocrText(
    text: String,
    ignoreCase: Boolean = true,
    language: String = SPConfig.lang,
    tessdata: String = System.getenv("TESSDATA_PREFIX"),
): WebElementCondition {
    return Condition.match("ocrText") {
        return@match Selenide.element(it).ocrText(language, tessdata).contains(text, ignoreCase)
    }
}

fun exactOcrText(
    text: String,
    ignoreCase: Boolean = true,
    language: String = SPConfig.lang,
    tessdata: String = System.getenv("TESSDATA_PREFIX"),
): WebElementCondition {
    return Condition.match("exactOcrText") {
        return@match Selenide.element(it).ocrText(language, tessdata).contentEquals(text, ignoreCase)
    }
}
