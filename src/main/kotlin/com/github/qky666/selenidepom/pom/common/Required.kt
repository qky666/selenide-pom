package com.github.qky666.selenidepom.pom.common

/**
 * Every field, property or method (without parameters) annotated in a [Loadable]
 * instance will be checked if visible when [shouldLoadRequired] or [hasLoadedRequired] is called.
 *
 * @property model Represents 'when' (with which `model`) the element is required.
 * Using default value means the element is always checked, regardless of `model` parameter
 * passed to [shouldLoadRequired] or [hasLoadedRequired].
 * You can set a value like "desktop" or "mobile" to represent that the element
 * ([com.codeborne.selenide.SelenideElement], [com.github.qky666.selenidepom.pom.web.Widget], etc.)
 * annotated is only required in the desktop or mobile version respectively of the web page.
 * ("desktop" or "mobile" are just examples, you can use any name you like)
 * @property lang Represents 'when' (with which `language`) the element is required.
 * Using default value means the element is always checked, regardless of `lang` parameter
 * passed to [shouldLoadRequired] or [hasLoadedRequired].
 * You can set a value like "en" or "es" to represent that the element
 * ([com.codeborne.selenide.SelenideElement], [com.github.qky666.selenidepom.pom.web.Widget], etc.)
 * annotated is only required in the english or spanish version respectively of the web page or screen.
 * ("en" or "es" are just examples, you can use any name you like)
 */
@MustBeDocumented
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Repeatable
annotation class Required(val model: String = "", val lang: String = "")
