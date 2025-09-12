package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.Command
import com.codeborne.selenide.DragAndDropOptions
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.FluentCommand
import com.codeborne.selenide.HoverOptions
import com.codeborne.selenide.ScrollIntoViewOptions
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.SetValueOptions
import com.codeborne.selenide.TypeOptions
import com.codeborne.selenide.WebElementCondition
import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
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
    protected open val container: SelenideElement? = self

    /**
     * Same as [SelenideElement.type] `(CharSequence(textToType))`.
     *
     * NOTE: Solves a problem added in selenide 6.17.0: If not present, Widget subclasses need to override this method.
     *
     * @param textToType the text to type
     * @return this
     */
    override fun type(textToType: CharSequence): Widget {
        self.type(textToType)
        return this
    }

    /**
     * Same as [SelenideElement.type] `(TypeOptions(typeOptions))`.
     *
     * NOTE: Solves a problem added in selenide 6.17.0: If not present, Widget subclasses need to override this method.
     *
     * @param typeOptions the type options
     * @return this
     */
    override fun type(typeOptions: TypeOptions): Widget {
        self.type(typeOptions)
        return this
    }

    /**
     * Same as [SelenideElement.getOptions].
     *
     * NOTE: Solves a problem added in selenide 6.18.0: If not present, Widget subclasses need to override this method.
     *
     * @return the [ElementsCollection] found
     */
    override fun getOptions() = self.options

    /**
     * Same as [SelenideElement.execute] `(Command(command))`.
     *
     * NOTE: Solves a problem added in selenide 7.7.3: If not present, Widget subclasses need to override this method.
     *
     * @param command the command
     * @return the [ReturnType] obtained
     */
    override fun <ReturnType : Any> execute(command: Command<ReturnType>) = self.execute(command)

    /**
     * Same as [SelenideElement.execute] `(Command(command), (Duration(timeout))`.
     *
     * NOTE: Solves a problem added in selenide 7.7.3: If not present, Widget subclasses need to override this method.
     *
     * @param command the command
     * @param timeout the timeout
     * @return the [ReturnType] obtained
     */
    override fun <ReturnType : Any> execute(command: Command<ReturnType>, timeout: Duration) =
        self.execute(command, timeout)

    /**
     * Same as [SelenideElement.execute] `(FluentCommand(command))`.
     *
     * NOTE: Solves a problem added in selenide 7.7.3: If not present, Widget subclasses need to override this method.
     *
     * @param command the command
     * @return the [ReturnType] obtained
     */
    override fun <ReturnType : SelenideElement> execute(command: FluentCommand): ReturnType = self.execute(command)

    /**
     * Same as [SelenideElement.find] `(By.xpath(xpathExpression))`.
     *
     * @param xpathExpression the xpath
     * @return the [SelenideElement] found
     */
    fun findX(xpathExpression: String) = find(By.xpath(xpathExpression))

    /**
     * Same as [SelenideElement.find] `(By.xpath(xpathExpression), index)`.
     *
     * @param xpathExpression the xpath
     * @return the [SelenideElement] found
     */
    fun findX(xpathExpression: String, index: Int) = find(By.xpath(xpathExpression), index)

    /**
     * Same as [SelenideElement.findAll] `(By.xpath(xpathExpression))`.
     *
     * @param xpathExpression the xpath
     * @return the [ElementsCollection] found
     */
    fun findXAll(xpathExpression: String) = findAll(By.xpath(xpathExpression))

    /**
     * Same as [SelenideElement.find] `(cssSelector)`.
     * Note: method overrides to make it final and avoid IDE warnings.
     *
     * @param cssSelector the CSS selector
     * @return the [SelenideElement] found
     */
    final override fun find(cssSelector: String) = container?.find(cssSelector) ?: Page.find(cssSelector)

    /**
     * Same as [SelenideElement.find] `(seleniumSelector)`.
     * Note: method overrides to make it final and avoid IDE warnings.
     *
     * @param seleniumSelector the selector
     * @return the [SelenideElement] found
     */
    final override fun find(seleniumSelector: By) = container?.find(seleniumSelector) ?: Page.find(seleniumSelector)

    /**
     * Same as [SelenideElement.find] `(cssSelector, index)`.
     * Note: method overrides to make it final and avoid IDE warnings.
     *
     * @param cssSelector the CSS selector
     * @return the [SelenideElement] found
     */
    final override fun find(cssSelector: String, index: Int) =
        container?.find(cssSelector, index) ?: Page.find(cssSelector, index)

    /**
     * Same as [SelenideElement.find] `(seleniumSelector, index)`.
     * Note: method overrides to make it final and avoid IDE warnings.
     *
     * @param seleniumSelector the selector
     * @return the [SelenideElement] found
     */
    final override fun find(seleniumSelector: By, index: Int) =
        container?.find(seleniumSelector, index) ?: Page.find(seleniumSelector, index)

    /**
     * Same as [SelenideElement.findAll] `(cssSelector)`.
     * Note: method overrides to make it final and avoid IDE warnings.
     *
     * @param cssSelector the CSS selector
     * @return the [ElementsCollection] found
     */
    final override fun findAll(cssSelector: String) = container?.findAll(cssSelector) ?: Page.findAll(cssSelector)

    /**
     * Same as [SelenideElement.findAll] `(seleniumSelector)`.
     * Note: method overrides to make it final and avoid IDE warnings.
     *
     * @param seleniumSelector the selector
     * @return the [ElementsCollection] found
     */
    final override fun findAll(seleniumSelector: By) =
        container?.findAll(seleniumSelector) ?: Page.findAll(seleniumSelector)

    override fun getDomProperty(name: String): String? {
        return self.getDomProperty(name)
    }

    override fun getDomAttribute(name: String): String? {
        return self.getDomAttribute(name)
    }

    override fun getAriaRole(): String? {
        return self.ariaRole
    }

    override fun getAccessibleName(): String? {
        return self.accessibleName
    }

    override fun getShadowRoot(): SearchContext {
        return self.shadowRoot
    }
}

/**
 * Returns the [SelenideElement] as a [T] instance.
 *
 * @param factory method that obtains a [T] instance from a [SelenideElement] (usually a [T] constructor)
 * @return [T] instance based on provided [SelenideElement]
 */
@Suppress("UNCHECKED_CAST")
fun <T : Widget> SelenideElement.asWidget(factory: (e: SelenideElement) -> T) = this as? T ?: factory(this)

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
fun <T : Widget> T.widgetShould(condition: WebElementCondition, timeout: Duration): T {
    this.should(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.should], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShould(vararg condition: WebElementCondition): T {
    this.should(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldHave(vararg condition: WebElementCondition): T {
    this.shouldHave(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldHave(condition: WebElementCondition, timeout: Duration): T {
    this.shouldHave(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldBe(vararg condition: WebElementCondition): T {
    this.shouldBe(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldBe(condition: WebElementCondition, timeout: Duration): T {
    this.shouldBe(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNot], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNot(vararg condition: WebElementCondition): T {
    this.shouldNot(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNot], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNot(condition: WebElementCondition, timeout: Duration): T {
    this.shouldNot(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNotHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNotHave(vararg condition: WebElementCondition): T {
    this.shouldNotHave(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNotHave], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNotHave(condition: WebElementCondition, timeout: Duration): T {
    this.shouldNotHave(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement.shouldNotBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNotBe(vararg condition: WebElementCondition): T {
    this.shouldNotBe(*condition)
    return this
}

/**
 * Same as [SelenideElement.shouldNotBe], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.widgetShouldNotBe(condition: WebElementCondition, timeout: Duration): T {
    this.shouldNotBe(condition, timeout)
    return this
}

/**
 * Same as [SelenideElement]'s "as" method, but returns a [Widget] subclass instance instead of [SelenideElement]
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
fun <T : Widget> T.scrollWidgetIntoView(options: ScrollIntoViewOptions): T {
    this.scrollIntoView(options)
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
 * Same as [SelenideElement.dragAndDrop], but returns a [Widget] subclass instance instead of [SelenideElement]
 */
fun <T : Widget> T.dragWidgetAndDrop(options: DragAndDropOptions): T {
    this.dragAndDrop(options)
    return this
}
