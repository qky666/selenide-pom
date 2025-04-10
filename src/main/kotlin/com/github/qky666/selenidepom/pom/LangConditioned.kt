package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebElementCondition
import com.github.qky666.selenidepom.config.SPConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.Duration

private val logger = KotlinLogging.logger {}

interface LangConditioned : SelenideElement, Loadable {
    val conditions: Map<String, WebElementCondition>
    val strict: Boolean

    /**
     * Returns true if the element meets the expected [WebElementCondition] for a given language [lang].
     *
     * @param lang language used to get the condition to verify. Default value: `SPConfig.lang`
     * @param strict if strict is `true`, a [ConditionNotDefinedError] is thrown if no condition is found for given [lang]
     * @return true if the element meets the expected [WebElementCondition] for a given language [lang], false otherwise
     */
    fun meetsCondition(lang: String = SPConfig.lang, strict: Boolean = this.strict) = try {
        this.has(conditions.getValue(lang))
    } catch (e: NoSuchElementException) {
        !strict
    }
}

/**
 * Verifies that the element meets the expected [WebElementCondition] for a given language [lang].
 *
 * @param timeout the timeout of the operation. Default value: Configured timeout in `Selenide`
 * @param lang language used to get the condition to verify. Default value: `SPConfig.lang`
 * @param strict if strict is `true`, a [ConditionNotDefinedError] is thrown if no condition is found for given [lang]
 * @return `this`, so it can be chained
 * @throws ConditionNotDefinedError if no condition is found for given [lang] and [strict] is `true`
 */
fun <T : LangConditioned> T.shouldMeetCondition(
    timeout: Duration = Duration.ofMillis(SPConfig.selenideConfig.timeout()),
    lang: String = SPConfig.lang,
    strict: Boolean = this.strict,
): LangConditioned {
    try {
        val condition = conditions.getValue(lang)
        this.should(condition, timeout)
        val elementLog = this.toString().replace("\n", "\\n")
        logger.debug { "Checked condition '$condition' (language '$lang') in element '$elementLog'" }
    } catch (e: NoSuchElementException) {
        if (strict) throw ConditionNotDefinedError(this, lang)
    }
    return this
}
