package com.github.qky666.selenidepom.kotlin

import com.codeborne.selenide.SelenideElement

/**
 * Abstract class that implements [RequiredContainer] interface that has a [SelenideElement] 'self' field.
 */
abstract class Widget (open val self: SelenideElement) : RequiredContainer
