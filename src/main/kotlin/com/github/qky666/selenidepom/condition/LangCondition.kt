package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.CheckResult
import com.codeborne.selenide.Condition
import com.codeborne.selenide.Driver
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.ConditionNotDefinedError
import com.github.qky666.selenidepom.pom.LangConditionedElement
import org.openqa.selenium.WebElement

fun langCondition(conditions: Map<String, Condition>, strict: Boolean = true): Condition {
    return object : Condition("langCondition") {
        override fun check(driver: Driver, element: WebElement): CheckResult {
            return try {
                conditions.getValue(SPConfig.lang).check(driver, element)
            } catch (e: NoSuchElementException) {
                val selenideElement = LangConditionedElement(Selenide.element(element), conditions)
                if (strict) throw ConditionNotDefinedError(selenideElement, SPConfig.lang)
                else CheckResult(true, null)
            }
        }
    }
}

@Suppress("RedundantValueArgument")
fun langCondition(conditions: Map<String, String>): Condition {
    return langCondition(conditions.mapValues { Condition.exactText(it.value) }, true)
}
