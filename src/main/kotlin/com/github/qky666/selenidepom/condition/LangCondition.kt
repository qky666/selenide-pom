package com.github.qky666.selenidepom.condition

import com.codeborne.selenide.Condition
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.pom.ConditionNotDefinedError
import com.github.qky666.selenidepom.pom.Page

/**
 * Some functions that create [WebElementCondition] related to current [SPConfig.lang].
 */
object LangCondition {
    /**
     * Returns the [WebElementCondition] corresponding to current [SPConfig.lang] in [conditions].
     *
     * @param conditions a [Map] where the keys are possible [SPConfig.lang] values, each of then have a [WebElementCondition] associated
     * @param strict If `true`, when current [SPConfig.lang] is not present in [conditions] keys, a [ConditionNotDefinedError] is thrown when the returned [WebElementCondition] is evaluated. If `false`, no error is thrown and the condition returned is considered to be always met. Default value: `true`
     * @return the [WebElementCondition] corresponding to current [SPConfig.lang] in [conditions]
     */
    fun langCondition(conditions: Map<String, WebElementCondition>, strict: Boolean = true): WebElementCondition {
        return Condition.match("Condition associated to current lang in `conditions`: $conditions") { webElement ->
            val element = Page.find(webElement)
            conditions[SPConfig.lang]?.let { element.has(it) } ?: if (strict) throw ConditionNotDefinedError(
                element, SPConfig.lang
            ) else true
        }
    }

    /**
     * Returns the [WebElementCondition] created using [Condition.exactText] corresponding to current [SPConfig.lang] in [conditions].
     * [WebElementCondition] returned is considered `strict`: if current [SPConfig.lang] is not present in [conditions] keys,
     * a [ConditionNotDefinedError] is thrown when the returned [WebElementCondition] is evaluated.
     *
     * @param conditions a [Map] where the keys are possible [SPConfig.lang] values, each of then have a [String] associated used to create a [WebElementCondition] using [Condition.exactText]
     * @return the [WebElementCondition] created corresponding to current [SPConfig.lang] in [conditions]
     */
    @Suppress("RedundantValueArgument")
    fun langCondition(conditions: Map<String, String>): WebElementCondition {
        return langCondition(conditions.mapValues { Condition.exactText(it.value) }, true)
    }
}
