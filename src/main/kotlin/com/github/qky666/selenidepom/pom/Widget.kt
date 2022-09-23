package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.*
import org.openqa.selenium.WebElement
import java.time.Duration

/**
 * Abstract class that implements [SelenideElement] and [Loadable] interfaces.
 * @sample com.github.qky666.selenidepom.sample.newWidgetSubclassSample
 */
abstract class Widget(private val self: SelenideElement) : Loadable, SelenideElement by self

/**
 * Same as [SelenideElement.setValue], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetSetValue(text: String): T {
    this.value = text
    return this
}

/**
* Same as [SelenideElement.val], but returns a [Widget] subclass instance instead of [SelenideElement]
*/
fun <T: Widget>T.widgetVal(text: String): T {
    this.`val`(text)
    return this
}

/**
 * Same as [SelenideElement.setValue], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetSetValue(text: SetValueOptions): T {
    this.setValue(text)
    return this
}

/**
 * Same as [SelenideElement.append], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetAppend(text: String): T {
    this.append(text)
    return this
}

/**
 * Same as [SelenideElement.pressEnter], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetPressEnter(): T {
    this.pressEnter()
    return this
}

/**
 * Same as [SelenideElement.pressTab], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetPressTab(): T {
    this.pressTab()
    return this
}

/**
 * Same as [SelenideElement.pressEscape], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetPressEscape(): T {
    this.pressEscape()
    return this
}

/**
 * Same as [SelenideElement.setSelected], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetSetSelected(selected: Boolean): T {
    this.isSelected = selected
    return this
}

/**
 * Same as [SelenideElement.should], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShould(condition: Condition, timeout: Duration): T {
    this.should(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.should], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShould(vararg condition: Condition): T {
    this.should(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldHave(vararg condition: Condition): T {
    this.shouldHave(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldHave(condition: Condition, timeout: Duration): T {
    this.shouldHave(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldBe(vararg condition: Condition): T {
    this.shouldBe(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldBe(condition: Condition, timeout: Duration): T {
    this.shouldBe(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNot], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldNot(vararg condition: Condition): T {
    this.shouldNot(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNot], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldNot(condition: Condition, timeout: Duration): T {
    this.shouldNot(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNotHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldNotHave(vararg condition: Condition): T {
    this.shouldNotHave(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNotHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldNotHave(condition: Condition, timeout: Duration): T {
    this.shouldNotHave(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNotBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldNotBe(vararg condition: Condition): T {
    this.shouldNotBe(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNotBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetShouldNotBe(condition: Condition, timeout: Duration): T {
    this.shouldNotBe(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.as], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetAs(alias: String): T {
    this.`as`(alias)
    return this
}

/**
 * Same as [SelenideElement.scrollTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetScrollTo(): T {
    this.scrollTo()
    return this
}

/**
 * Same as [SelenideElement.scrollIntoView], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetScrollIntoView(scrollIntoViewOptions: String): T {
    this.scrollIntoView(scrollIntoViewOptions)
    return this
}

/**
 * Same as [SelenideElement.scrollIntoView], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetScrollIntoView(alignToTop: Boolean): T {
    this.scrollIntoView(alignToTop)
    return this
}

/**
 * Same as [SelenideElement.contextClick], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetContextClick(): T {
    this.contextClick()
    return this
}

/**
 * Same as [SelenideElement.doubleClick], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetDoubleClick(): T {
    this.doubleClick()
    return this
}

/**
 * Same as [SelenideElement.hover], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetHover(options: HoverOptions): T {
    this.hover(options)
    return this
}

/**
 * Same as [SelenideElement.hover], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetHover(): T {
    this.hover()
    return this
}

/**
 * Same as [SelenideElement.dragAndDropTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetDragAndDropTo(targetCssSelector: String, options: DragAndDropOptions): T {
    this.dragAndDropTo(targetCssSelector, options)
    return this
}

/**
 * Same as [SelenideElement.dragAndDropTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetDragAndDropTo(target: WebElement): T {
    this.dragAndDropTo(target)
    return this
}

/**
 * Same as [SelenideElement.dragAndDropTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T: Widget>T.widgetDragAndDropTo(targetCssSelector: String): T {
    this.dragAndDropTo(targetCssSelector)
    return this
}
