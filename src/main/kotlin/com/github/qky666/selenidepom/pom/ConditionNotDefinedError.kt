package com.github.qky666.selenidepom.pom

/**
 * Error thrown when a [LangConditionedElement.shouldMeetCondition] is executed with parameter `strict` as `true` and
 * no [com.codeborne.selenide.Condition] is found for given `lang`.
 *
 * @param element the [LangConditionedElement] where error is generated
 * @param lang the language that has not been found in [LangConditionedElement]
 */
class ConditionNotDefinedError(element: LangConditionedElement, lang: String) :
    Error("Text not defined for language $lang in TextElement ${element.searchCriteria}")
