package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.config.SPConfig
import mu.KotlinLogging
import java.time.Duration

/**
 * Represents a [SelenideElement] with a 'Language (String) -> [Condition]' map associated, so it is possible to check
 * that the element meets the condition given a determined language.
 * It is intended to be used to verify that the element's text is correct.
 * Can be helpful in all kind of web pages, but specially in those that are available in more than one language.
 *
 * @param self the [SelenideElement]
 * @param conditions the 'Language (String) -> [Condition]' map with the expected [Condition] for [self] in each language
 * @param strict default value for `strict` in [shouldMeetCondition]
 */
class LangConditionedElement(
    private val self: SelenideElement,
    private val conditions: Map<String, Condition>,
    private val strict: Boolean = true
) : SelenideElement by self {

    /**
     * Creates a new instance using provided [exactTexts].
     * The [LangConditionedElement] created is the same as if [Condition.exactText] is applied to every value in [exactTexts].
     *
     * @param self the [SelenideElement]
     * @param exactTexts the map of expected exact text for [self] in each language
     */
    constructor(self: SelenideElement, exactTexts: Map<String, String>) : this(
        self, exactTexts.mapValues { Condition.exactText(it.value) }, true
    )

    /**
     * Creates a new instance using provided [condition] for all languages.
     * Useful if the web page uses only one language or the element's text
     * (if, for example, provided condition affects the element's text) is the same in all languages.
     *
     * @param self the [SelenideElement]
     * @param condition the expected [Condition] for [self] in every language
     * @param strict default value for `strict` in [shouldMeetCondition]
     */
    constructor(self: SelenideElement, condition: Condition, strict: Boolean = true) : this(
        self, mapOf<String, Condition>().withDefault { condition }, strict
    )

    /**
     * Creates a new instance using provided [exactText] for all languages.
     * Useful if the web page uses only one language or the element's exact text is the same in all languages.
     *
     * @param self the [SelenideElement]
     * @param exactText the expected [Condition.exactText] for [self] in every language
     * @param strict default value for `strict` in [shouldMeetCondition]
     */
    constructor(self: SelenideElement, exactText: String, strict: Boolean = true) : this(
        self, mapOf<String, Condition>().withDefault { Condition.exactText(exactText) }, strict
    )

    private val logger = KotlinLogging.logger {}

    /**
     * Verifies that [self] meets the expected [Condition] for a given language [lang].
     *
     * @param timeout the timeout of the operation. Default value: Configured timeout in `Selenide`
     * @param lang language used to get the condition to verify. Default value: `SPConfig.lang`
     * @param strict if strict is `true`, a [ConditionNotDefinedError] is thrown if no condition is found for given [lang]
     * @return `this`, so it can be chained
     * @throws ConditionNotDefinedError if no condition is found for given [lang] and [strict] is `true`
     */
    fun shouldMeetCondition(
        timeout: Duration = Duration.ofMillis(SPConfig.selenideConfig.timeout()),
        lang: String = SPConfig.lang,
        strict: Boolean = this.strict
    ): LangConditionedElement {
        try {
            val condition = conditions.getValue(lang)
            self.should(condition, timeout)
            logger.debug {
                "Checked condition '$condition' (language '$lang') in element '${
                    this.toString().replace("\n", "\\n")
                }'"
            }
        } catch (e: NoSuchElementException) {
            if (strict) {
                throw ConditionNotDefinedError(this, lang)
            }
        }
        return this
    }

    fun meetsCondition(lang: String = SPConfig.lang, strict: Boolean = this.strict): Boolean {
        return try {
            val condition = conditions.getValue(lang)
            self.has(condition)
        } catch (e: NoSuchElementException) {
            !strict
        }
    }
}
