@file:Suppress("unused")

package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.WebElementCondition
import com.codeborne.selenide.appium.SelenideAppiumElement
import org.openqa.selenium.SearchContext

/**
 * Represents a [SelenideAppiumElement] with a 'Language (String) -> [WebElementCondition]' map associated,
 * so it is possible to check that the element meets the condition given a determined language.
 * It is intended to be used to verify that the element's text is correct.
 * It can be helpful in all kinds of web pages, but specially in those that are available in more than one language.
 *
 * @param self the [SelenideAppiumElement]
 * @param conditions the 'Language (String) -> [WebElementCondition]' map with the expected [WebElementCondition] for [self] in each language
 * @param strict if true, when a given language (key) does not exist in [conditions] it is considered that condition is not met
 */
class LangConditionedAppiumElement(
    private val self: SelenideAppiumElement,
    override val conditions: Map<String, WebElementCondition>,
    override val strict: Boolean = true,
) : SelenideAppiumElement by self, LangConditioned {

    /**
     * Creates a new instance using provided [exactTexts].
     * The [LangConditionedAppiumElement] created is the same as if [Condition.exactText]
     * is applied to every value in [exactTexts].
     *
     * @param self the [SelenideAppiumElement]
     * @param exactTexts the map of expected exact text for [self] in each language
     */
    @Suppress("RedundantValueArgument")
    constructor(self: SelenideAppiumElement, exactTexts: Map<String, String>) : this(
        self, exactTexts.mapValues { Condition.exactText(it.value) }, true
    )

    /**
     * Creates a new instance using provided [condition] for all languages.
     * Useful if the web page uses only one language or the element's text
     * (if, for example, provided condition affects the element's text) is the same in all languages.
     *
     * @param self the [SelenideAppiumElement]
     * @param condition the expected [WebElementCondition] for [self] in every language
     * @param strict if true, when a given language (key) does not exist in [conditions] it is considered that condition is not met
     */
    constructor(self: SelenideAppiumElement, condition: WebElementCondition, strict: Boolean = true) : this(
        self, mapOf<String, WebElementCondition>().withDefault { condition }, strict
    )

    /**
     * Creates a new instance using provided [exactText] for all languages.
     * Useful if the web page uses only one language or the element's exact text is the same in all languages.
     *
     * @param self the [SelenideAppiumElement]
     * @param exactText the expected [Condition.exactText] for [self] in every language
     * @param strict if true, when a given language (key) does not exist in [conditions] it is considered that condition is not met
     */
    constructor(self: SelenideAppiumElement, exactText: String, strict: Boolean = true) : this(
        self, mapOf<String, WebElementCondition>().withDefault { Condition.exactText(exactText) }, strict
    )

    override fun getDomProperty(name: String): String? {
        return self.getDomProperty(name)
    }

    override fun getDomAttribute(name: String): String? {
        return self.getDomAttribute(name)
    }

    override fun getAriaRole(): String? {
        return self.ariaRole
    }

    override fun getAccessibleName(): String? {
        return self.accessibleName
    }

    override fun getShadowRoot(): SearchContext {
        return self.shadowRoot
    }
}
