package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement

/**
 * See
 * https://stackoverflow.com/questions/26992039/what-is-the-proper-way-to-create-new-instance-of-generic-class-in-kotlin
 */
private fun <T : Widget> widgetFactory(element: SelenideElement, factory: (e: SelenideElement) -> T): T {
    return factory(element)
}

/**
 * Same as [ElementsCollection.find], but returns a [Widget] subclass instance instead of [SelenideElement].
 *
 * For example:
 * ```
 * class MyWidget(self: SelenideElement) : Widget(self)
 * val element: MyWidget = Selenide.elements("button").findWidget(disabled, ::MyWidget )
 * ```
 *
 * @param T Widget subclass returned
 * @param condition The condition used to find the element
 * @param factory The factory function (usually a `T` constructor) used to create the returned object
 * @return The element found (as a Widget subclass of type `T`)
 */
fun <T : Widget> ElementsCollection.findWidget(condition: Condition, factory: (e: SelenideElement) -> T): T {
    return widgetFactory(this.find(condition), factory)
}

/**
 * Same as [ElementsCollection.get], but returns a [Widget] subclass instance instead of [SelenideElement].
 *
 * For example:
 * ```
 * class MyWidget(self: SelenideElement) : Widget(self)
 * val element: MyWidget = Selenide.elements("button").getWidget(2, ::MyWidget)
 * ```
 *
 * @param T Widget subclass returned
 * @param index The index used to find the element
 * @param factory The factory function (usually a `T` constructor) used to create the returned object
 * @return The element found (as a Widget subclass of type `T`)
 */
fun <T : Widget> ElementsCollection.getWidget(index: Int, factory: (e: SelenideElement) -> T): T {
    return widgetFactory(this[index], factory)
}

/**
 * Same as [ElementsCollection.first], but returns a [Widget] subclass instance instead of [SelenideElement].
 *
 * For example:
 * ```
 * class MyWidget(self: SelenideElement) : Widget(self)
 * val element: MyWidget = Selenide.elements("button").firstWidget(::MyWidget)
 * ```
 *
 * @param T Widget subclass returned
 * @param factory The factory function (usually a `T` constructor) used to create the returned object
 * @return The element found (as a Widget subclass of type `T`)
 */
fun <T : Widget> ElementsCollection.firstWidget(factory: (e: SelenideElement) -> T): T {
    return widgetFactory(this.first(), factory)
}

/**
 * Same as [ElementsCollection.last], but returns a [Widget] subclass instance instead of [SelenideElement].
 *
 * For example:
 * ```
 * class MyWidget(self: SelenideElement) : Widget(self)
 * val element: MyWidget = Selenide.elements("button").lastWidget(::MyWidget)
 * ```
 *
 * @param T Widget subclass returned
 * @param factory The factory function (usually a `T` constructor) used to create the returned object
 * @return The element found (as a Widget subclass of type `T`)
 */
fun <T : Widget> ElementsCollection.lastWidget(factory: (e: SelenideElement) -> T): T {
    return widgetFactory(this.last(), factory)
}
