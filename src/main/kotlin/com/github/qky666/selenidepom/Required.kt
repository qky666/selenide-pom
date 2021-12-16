package com.github.qky666.selenidepom

/**
 * Every field, property or method (without paramenters) annotated in a [Page] subclass will be checked if visible
 * when [Page.shouldLoadRequired] or [Page.hasLoadedRequired] is called.
 *
 * @property value Represents 'when' the element is required.
 * Using default value means the element is always checked, regardless of pomVersion parameter
 * passed to [Page.shouldLoadRequired] or [Page.hasLoadedRequired].
 * You can set a value like "desktop" or "mobile" to represent that the web element
 * ([com.codeborne.selenide.SelenideElement], [Widget], etc.) annotated is only required in the desktop
 * or mobile version respectively of the web page.
 */
@MustBeDocumented
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Repeatable
annotation class Required(val value: String = "")
