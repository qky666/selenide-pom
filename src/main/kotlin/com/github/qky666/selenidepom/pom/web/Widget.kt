package com.github.qky666.selenidepom.pom.web

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.Condition
import com.codeborne.selenide.DragAndDropOptions
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.HoverOptions
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.SetValueOptions
import com.github.qky666.selenidepom.pom.common.Loadable
import org.openqa.selenium.By
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
 * @param self the [SelenideElement] that acts as a container of the elements defined inside the [Widget]
 * @constructor creates a new instance using provided [SelenideElement] as container
 */
abstract class Widget(private val self: SelenideElement) : SelenideElement by self, Loadable {

    /**
     * Same as [SelenideElement.find] `(By.xpath(xpathExpression))`.
     *
     * @param xpathExpression the xpath
     * @return the [SelenideElement] found
     */
    fun findX(xpathExpression: String): SelenideElement {
        return find(By.xpath(xpathExpression))
    }

    /**
     * Same as [SelenideElement.find] `(By.xpath(xpathExpression), index)`.
     *
     * @param xpathExpression the xpath
     * @return the [SelenideElement] found
     */
    fun findX(xpathExpression: String, index: Int): SelenideElement {
        return find(By.xpath(xpathExpression), index)
    }

    /**
     * Same as [SelenideElement.findAll] `(By.xpath(xpathExpression))`.
     *
     * @param xpathExpression the xpath
     * @return the [ElementsCollection] found
     */
    fun findXAll(xpathExpression: String): ElementsCollection {
        return findAll(By.xpath(xpathExpression))
    }

    /**
     * Same as [SelenideElement.find] `(cssSelector)`.
     * Note: method override to make it final and avoid IDE warnings.
     *
     * @param cssSelector the css selector
     * @return the [SelenideElement] found
     */
    final override fun find(cssSelector: String): SelenideElement {
        return self.find(cssSelector)
    }

    /**
     * Same as [SelenideElement.find] `(seleniumSelector)`.
     * Note: method override to make it final and avoid IDE warnings.
     *
     * @param seleniumSelector the selector
     * @return the [SelenideElement] found
     */
    final override fun find(seleniumSelector: By): SelenideElement {
        return self.find(seleniumSelector)
    }

    /**
     * Same as [SelenideElement.find] `(cssSelector, index)`.
     * Note: method override to make it final and avoid IDE warnings.
     *
     * @param cssSelector the css selector
     * @return the [SelenideElement] found
     */
    final override fun find(cssSelector: String, index: Int): SelenideElement {
        return self.find(cssSelector, index)
    }

    /**
     * Same as [SelenideElement.find] `(seleniumSelector, index)`.
     * Note: method override to make it final and avoid IDE warnings.
     *
     * @param seleniumSelector the selector
     * @return the [SelenideElement] found
     */
    final override fun find(seleniumSelector: By, index: Int): SelenideElement {
        return self.find(seleniumSelector, index)
    }

    /**
     * Same as [SelenideElement.findAll] `(cssSelector)`.
     * Note: method override to make it final and avoid IDE warnings.
     *
     * @param cssSelector the css selector
     * @return the [ElementsCollection] found
     */
    final override fun findAll(cssSelector: String): ElementsCollection {
        return self.findAll(cssSelector)
    }

    /**
     * Same as [SelenideElement.findAll] `(seleniumSelector)`.
     * Note: method override to make it final and avoid IDE warnings.
     *
     * @param seleniumSelector the selector
     * @return the [ElementsCollection] found
     */
    final override fun findAll(seleniumSelector: By): ElementsCollection {
        return self.findAll(seleniumSelector)
    }
}

/**
 * Returns the [SelenideElement] as a [T] instance.
 *
 * @param factory method that obtains a [T] instance from a [SelenideElement] (usually a [T] constructor)
 * @return [T] instance based on provided [SelenideElement]
 */
fun <T : Widget> SelenideElement.asWidget(factory: (e: SelenideElement) -> T): T {
    @Suppress("UNCHECKED_CAST")
    return this as? T ?: factory(this)
}

/**
 * Same as [SelenideElement.setValue], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.setWidgetValue(text: String): T {
    this.value = text
    return this
}

/**
 * Same as [SelenideElement.val], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetVal(text: String): T {
    this.`val`(text)
    return this
}

/**
 * Same as [SelenideElement.setValue], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.setWidgetValue(text: SetValueOptions): T {
    this.setValue(text)
    return this
}

/**
 * Same as [SelenideElement.append], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.appendToWidget(text: String): T {
    this.append(text)
    return this
}

/**
 * Same as [SelenideElement.pressEnter], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.pressEnterInWidget(): T {
    this.pressEnter()
    return this
}

/**
 * Same as [SelenideElement.pressTab], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.pressTabInWidget(): T {
    this.pressTab()
    return this
}

/**
 * Same as [SelenideElement.pressEscape], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.pressEscapeInWidget(): T {
    this.pressEscape()
    return this
}

/**
 * Same as [SelenideElement.setSelected], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.setSelectedWidget(selected: Boolean): T {
    this.isSelected = selected
    return this
}

/**
 * Same as [SelenideElement.should], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShould(condition: Condition, timeout: Duration): T {
    this.should(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.should], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShould(vararg condition: Condition): T {
    this.should(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldHave(vararg condition: Condition): T {
    this.shouldHave(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldHave(condition: Condition, timeout: Duration): T {
    this.shouldHave(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldBe(vararg condition: Condition): T {
    this.shouldBe(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldBe(condition: Condition, timeout: Duration): T {
    this.shouldBe(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNot], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNot(vararg condition: Condition): T {
    this.shouldNot(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNot], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNot(condition: Condition, timeout: Duration): T {
    this.shouldNot(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNotHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNotHave(vararg condition: Condition): T {
    this.shouldNotHave(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNotHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNotHave(condition: Condition, timeout: Duration): T {
    this.shouldNotHave(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNotBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNotBe(vararg condition: Condition): T {
    this.shouldNotBe(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNotBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNotBe(condition: Condition, timeout: Duration): T {
    this.shouldNotBe(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.as], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetAs(alias: String): T {
    this.`as`(alias)
    return this
}

/**
 * Same as [SelenideElement.scrollTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.scrollToWidget(): T {
    this.scrollTo()
    return this
}

/**
 * Same as [SelenideElement.scrollIntoView], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.scrollWidgetIntoView(scrollIntoViewOptions: String): T {
    this.scrollIntoView(scrollIntoViewOptions)
    return this
}

/**
 * Same as [SelenideElement.scrollIntoView], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.scrollWidgetIntoView(alignToTop: Boolean): T {
    this.scrollIntoView(alignToTop)
    return this
}

/**
 * Same as [SelenideElement.click], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.clickWidget(clickOption: ClickOptions): T {
    this.click(clickOption)
    return this
}

/**
 * Same as [SelenideElement.contextClick], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.contextClickWidget(): T {
    this.contextClick()
    return this
}

/**
 * Same as [SelenideElement.doubleClick], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.doubleClickWidget(): T {
    this.doubleClick()
    return this
}

/**
 * Same as [SelenideElement.hover], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.hoverWidget(options: HoverOptions): T {
    this.hover(options)
    return this
}

/**
 * Same as [SelenideElement.hover], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.hoverWidget(): T {
    this.hover()
    return this
}

/**
 * Same as [SelenideElement.dragAndDropTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.dragWidgetAndDropTo(targetCssSelector: String, options: DragAndDropOptions): T {
    this.dragAndDropTo(targetCssSelector, options)
    return this
}

/**
 * Same as [SelenideElement.dragAndDropTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.dragWidgetAndDropTo(target: WebElement): T {
    this.dragAndDropTo(target)
    return this
}

/**
 * Same as [SelenideElement.dragAndDropTo], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.dragWidgetAndDropTo(targetCssSelector: String): T {
    this.dragAndDropTo(targetCssSelector)
    return this
}