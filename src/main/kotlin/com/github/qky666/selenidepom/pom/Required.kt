package com.github.qky666.selenidepom.pom

/**
 * Every field, property or method (without parameters) annotated in a [Loadable]
 * instance will be checked if visible when [shouldLoadRequired] or [hasLoadedRequired] is called.
 *
 * @property model Represents 'when' (with which `model`) the element is required. Using default value means the element is always checked, regardless of `model` parameter passed to [shouldLoadRequired] or [hasLoadedRequired]. You can set a value like "desktop" or "mobile" to represent that the element ([com.codeborne.selenide.SelenideElement], [Widget], etc.) annotated is only required in the desktop or mobile version respectively of the web page. ("desktop" or "mobile" are just examples, you can use any name you like)
 * @property lang Represents 'when' (with which `language`) the element is required. Using default value means the element is always checked, regardless of `lang` parameter passed to [shouldLoadRequired] or [hasLoadedRequired]. You can set a value like "en" or "es" to represent that the element ([com.codeborne.selenide.SelenideElement], [Widget], etc.) annotated is only required in the english or spanish version respectively of the web page or screen. ("en" or "es" are just examples, you can use any name you like)
 * @property scroll Scrolls to the required element before checking it. Can be useful in web pages with elements that are present (and have a position) but are 'hidden' (invisible) until the browser scrolls to their position.
 * @property scrollString The string passed to [com.codeborne.selenide.SelenideElement.scrollIntoView]. Ignored if [scroll] is false.
 */
@MustBeDocumented
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Repeatable
annotation class Required(
    val model: String = "",
    val lang: String = "",
    val scroll: Boolean = false,
    val scrollString: String = "{behavior: \"auto\", block: \"center\", inline: \"center\"}",
)
