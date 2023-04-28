package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.CheckResult
import com.codeborne.selenide.Condition
import com.codeborne.selenide.Driver
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.common.ConditionNotDefinedError
import com.github.qky666.selenidepom.pom.web.LangConditionedElement
import org.openqa.selenium.WebElement

fun langCondition(conditions: Map<String, Condition>, strict: Boolean = true): Condition {
    return object : Condition("langCondition") {
        override fun check(driver: Driver, element: WebElement): CheckResult {
            return try {
                conditions.getValue(SPConfig.lang).check(driver, element)
            } catch (e: NoSuchElementException) {
                if (strict) {
                    throw ConditionNotDefinedError(
                        LangConditionedElement(Selenide.element(element), conditions),
                        SPConfig.lang
                    )
                } else {
                    CheckResult(true, null)
                }
            }
        }
    }
}

fun langCondition(conditions: Map<String, String>): Condition {
    return langCondition(conditions.mapValues { Condition.exactText(it.value) }, true)
}
