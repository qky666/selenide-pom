package com.github.qky666.selenidepom

import com.codeborne.selenide.SelenideElement

/**
 * Abstract [Page] subclass that has a [SelenideElement] 'self' field.
 */
abstract class Widget (open val self: SelenideElement) : Page()
