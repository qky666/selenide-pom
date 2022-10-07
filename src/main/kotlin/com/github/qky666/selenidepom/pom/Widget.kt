@file:Suppress("UNUSED_PARAMETER")

package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.*
import org.openqa.selenium.WebElement
import java.time.Duration

/**
 * Abstract class that implements [SelenideElement] and [Loadable] interfaces.
 * Instances can be interpreted as a section of a web page.
 *
 * For example:
 * ```
 * class MyWidget(self: SelenideElement) : Widget(self) {
 *   val inputField = this.find("input")
 *   @Required val submit = this.find("button")
 *   fun pressSubmit() {
 *     submit.click()
 *   }
 * }
 * ```
 *
 * @param self The [SelenideElement] that acts as a container of the elements defined inside the Widget
 * @constructor Creates a new instance using provided [SelenideElement] as container
 */
abstract class Widget(private val self: SelenideElement) : Loadable, SelenideElement by self

fun <T : Widget> SelenideElement.asWidget(factory: (e: SelenideElement) -> T): T {
    @Suppress("UNCHECKED_CAST")
    return this as? T ?: factory(this)
}

/**
 * Same as [SelenideElement.setValue], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.setValue(text: String, ignored: Unit): T {
    this.value = text
    return this
}

/**
 * Same as [SelenideElement.val], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.`val`(text: String, ignored: Unit): T {
    this.`val`(text)
    return this
}

/**
 * Same as [SelenideElement.setValue], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.setValue(text: SetValueOptions, ignored: Unit): T {
    this.setValue(text)
    return this
}

/**
 * Same as [SelenideElement.append], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.append(text: String, ignored: Unit): T {
    this.append(text)
    return this
}

/**
 * Same as [SelenideElement.pressEnter], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.pressEnter(ignored: Unit): T {
    this.pressEnter()
    return this
}

/**
 * Same as [SelenideElement.pressTab], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.pressTab(ignored: Unit): T {
    this.pressTab()
    return this
}

/**
 * Same as [SelenideElement.pressEscape], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.pressEscape(ignored: Unit): T {
    this.pressEscape()
    return this
}

/**
 * Same as [SelenideElement.setSelected], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.setSelected(selected: Boolean, ignored: Unit): T {
    this.isSelected = selected
    return this
}

/**
 * Same as [SelenideElement.should], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.should(condition: Condition, timeout: Duration, ignored: Unit): T {
    this.should(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.should], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.should(vararg condition: Condition, ignored: Unit): T {
    this.should(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldHave(vararg condition: Condition, ignored: Unit): T {
    this.shouldHave(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldHave(condition: Condition, timeout: Duration, ignored: Unit): T {
    this.shouldHave(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldBe(vararg condition: Condition, ignored: Unit): T {
    this.shouldBe(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldBe(condition: Condition, timeout: Duration, ignored: Unit): T {
    this.shouldBe(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNot], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldNot(vararg condition: Condition, ignored: Unit): T {
    this.shouldNot(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNot], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldNot(condition: Condition, timeout: Duration, ignored: Unit): T {
    this.shouldNot(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNotHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldNotHave(vararg condition: Condition, ignored: Unit): T {
    this.shouldNotHave(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNotHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldNotHave(condition: Condition, timeout: Duration, ignored: Unit): T {
    this.shouldNotHave(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNotBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldNotBe(vararg condition: Condition, ignored: Unit): T {
    this.shouldNotBe(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNotBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.shouldNotBe(condition: Condition, timeout: Duration, ignored: Unit): T {
    this.shouldNotBe(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement. as], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.`as`(alias: String, ignored: Unit): T {
    this.`as`(alias)
    return this
}

/**
 * Same as [SelenideElement.scrollTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.scrollTo(ignored: Unit): T {
    this.scrollTo()
    return this
}

/**
 * Same as [SelenideElement.scrollIntoView], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.scrollIntoView(scrollIntoViewOptions: String, ignored: Unit): T {
    this.scrollIntoView(scrollIntoViewOptions)
    return this
}

/**
 * Same as [SelenideElement.scrollIntoView], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.scrollIntoView(alignToTop: Boolean, ignored: Unit): T {
    this.scrollIntoView(alignToTop)
    return this
}

/**
 * Same as [SelenideElement.contextClick], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.contextClick(ignored: Unit): T {
    this.contextClick()
    return this
}

/**
 * Same as [SelenideElement.doubleClick], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.doubleClick(ignored: Unit): T {
    this.doubleClick()
    return this
}

/**
 * Same as [SelenideElement.hover], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.hover(options: HoverOptions, ignored: Unit): T {
    this.hover(options)
    return this
}

/**
 * Same as [SelenideElement.hover], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.hover(ignored: Unit): T {
    this.hover()
    return this
}

/**
 * Same as [SelenideElement.dragAndDropTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.dragAndDropTo(targetCssSelector: String, options: DragAndDropOptions, ignored: Unit): T {
    this.dragAndDropTo(targetCssSelector, options)
    return this
}

/**
 * Same as [SelenideElement.dragAndDropTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.dragAndDropTo(target: WebElement, ignored: Unit): T {
    this.dragAndDropTo(target)
    return this
}

/**
 * Same as [SelenideElement.dragAndDropTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.dragAndDropTo(targetCssSelector: String, ignored: Unit): T {
    this.dragAndDropTo(targetCssSelector)
    return this
}
