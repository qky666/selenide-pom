package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.Condition
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.ConditionNotDefinedError
import com.github.qky666.selenidepom.pom.Page

fun langCondition(conditions: Map<String, WebElementCondition>, strict: Boolean = true): WebElementCondition {
    return Condition.match("lang") { webElement ->
        val element = Page.find(webElement)
        conditions[SPConfig.lang]?.let { element.has(it) } ?: if (strict) throw ConditionNotDefinedError(
            element,
            SPConfig.lang
        ) else true
    }
}

@Suppress("RedundantValueArgument")
fun langCondition(conditions: Map<String, String>): WebElementCondition {
    return langCondition(conditions.mapValues { Condition.exactText(it.value) }, true)
}
