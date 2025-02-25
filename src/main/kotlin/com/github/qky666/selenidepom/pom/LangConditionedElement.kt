package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebElementCondition

/**
 * Represents a [SelenideElement] with a 'Language (String) -> [Condition]' map associated, so it is possible to check
 * that the element meets the condition given a determined language.
 * It is intended to be used to verify that the element's text is correct.
 * Can be helpful in all kind of web pages, but specially in those that are available in more than one language.
 *
 * @param self the [SelenideElement]
 * @param conditions the 'Language (String) -> [Condition]' map with the expected [Condition] for [self] in each language
 * @param strict if true, when a given language (key) does not exist in [conditions] it is considered that condition is not met
 */
open class LangConditionedElement(
    private val self: SelenideElement,
    override val conditions: Map<String, WebElementCondition>,
    override val strict: Boolean = true,
) : SelenideElement by self, LangConditioned {

    /**
     * Creates a new instance using provided [exactTexts].
     * The [LangConditionedElement] created is the same as if [Condition.exactText] is applied to every value
     * in [exactTexts].
     *
     * @param self the [SelenideElement]
     * @param exactTexts the map of expected exact text for [self] in each language
     */
    @Suppress("RedundantValueArgument")
    constructor(self: SelenideElement, exactTexts: Map<String, String>) : this(
        self, exactTexts.mapValues { Condition.exactText(it.value) }, true
    )

    /**
     * Creates a new instance using provided [condition] for all languages.
     * Useful if the web page uses only one language or the element's text
     * (if, for example, provided condition affects the element's text) is the same in all languages.
     *
     * @param self the [SelenideElement]
     * @param condition the expected [WebElementCondition] for [self] in every language
     * @param strict if true, when a given language (key) does not exist in [conditions] it is considered that condition is not met
     */
    constructor(self: SelenideElement, condition: WebElementCondition, strict: Boolean = true) : this(
        self, mapOf<String, WebElementCondition>().withDefault { condition }, strict
    )

    /**
     * Creates a new instance using provided [exactText] for all languages.
     * Useful if the web page uses only one language or the element's exact text is the same in all languages.
     *
     * @param self the [SelenideElement]
     * @param exactText the expected [Condition.exactText] for [self] in every language
     * @param strict if true, when a given language (key) does not exist in [conditions] it is considered that condition is not met
     */
    constructor(self: SelenideElement, exactText: String, strict: Boolean = true) : this(
        self, mapOf<String, WebElementCondition>().withDefault { Condition.exactText(exactText) }, strict
    )
}
