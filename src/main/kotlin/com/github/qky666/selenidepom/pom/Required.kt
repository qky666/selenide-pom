package com.github.qky666.selenidepom.pom

/**
 * Every field, property or method (without parameters) annotated in a [Loadable]
 * instance will be checked if visible when [shouldLoadRequired] or [hasLoadedRequired] is called.
 *
 * @property value Represents 'when' the element is required.
 * Using default value means the element is always checked, regardless of `pomVersion` parameter
 * passed to [shouldLoadRequired] or [hasLoadedRequired].
 * You can set a value like "desktop" or "mobile" to represent that the web element
 * ([com.codeborne.selenide.SelenideElement], [com.github.qky666.selenidepom.pom.Widget], etc.)
 * annotated is only required in the desktop or mobile version respectively of the web page.
 * ("desktop" or "mobile" are just examples, you can use any name you like)
 */
@MustBeDocumented
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Repeatable
annotation class Required(val value: String = "")
