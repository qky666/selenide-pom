@file:Suppress("unused")

package com.github.qky666.selenidepom.kotlin

import com.codeborne.selenide.ElementsContainer
import com.codeborne.selenide.SelenideElement

/**
 * [ElementsContainer] with a constructor that sets 'self' field.
 */
@Suppress("LeakingThis")
abstract class ElementsContainerWidget() : ElementsContainer(), RequiredContainer {
    constructor(self: SelenideElement) : this() {
    // Set self
        val superclass: Class<*> = ElementsContainerWidget::class.java.superclass
        val field = superclass.getDeclaredField("self")
        field.isAccessible = true
        field[this] = self
    }
}
