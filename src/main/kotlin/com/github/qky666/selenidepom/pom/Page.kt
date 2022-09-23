package com.github.qky666.selenidepom.pom

import com.github.qky666.selenidepom.annotation.Required

/**
 * Instances represent a whole web page.
 * A page can contain [com.codeborne.selenide.SelenideElement] and [Widget] that can be annotated as [Required].
 * See [Loadable].
 */
abstract class Page: Loadable
