package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.*
import com.github.qky666.selenidepom.annotation.Required
import org.openqa.selenium.WebElement
import java.time.Duration

/**
 * Abstract class that implements [SelenideElement] and [Loadable] interfaces.
 * @sample SampleCode.newWidgetSubclassSample
 */
abstract class Widget<ThisType : Widget<ThisType>>(private val self: SelenideElement) : Loadable,
    SelenideElement by self {

    private fun thisWithType(): ThisType {
        @Suppress("UNCHECKED_CAST") return this as ThisType
    }

    override fun setValue(text: String?): ThisType {
        self.value = text
        return thisWithType()
    }

    override fun setValue(text: SetValueOptions): ThisType {
        self.setValue(text)
        return thisWithType()
    }

    override fun `val`(text: String?): ThisType {
        self.`val`(text)
        return thisWithType()
    }

    override fun append(text: String): ThisType {
        self.append(text)
        return thisWithType()
    }

    override fun pressEnter(): ThisType {
        self.pressEnter()
        return thisWithType()
    }

    override fun pressTab(): ThisType {
        self.pressTab()
        return thisWithType()
    }

    override fun pressEscape(): ThisType {
        self.pressEscape()
        return thisWithType()
    }

    override fun setSelected(selected: Boolean): ThisType {
        self.isSelected = selected
        return thisWithType()
    }

    override fun should(condition: Condition, timeout: Duration): ThisType {
        self.should(condition, timeout)
        return thisWithType()
    }

    override fun should(vararg condition: Condition?): ThisType {
        self.should(*condition)
        return thisWithType()
    }

    override fun shouldHave(vararg condition: Condition?): ThisType {
        self.shouldHave(*condition)
        return thisWithType()
    }

    override fun shouldHave(condition: Condition, timeout: Duration): ThisType {
        self.shouldHave(condition, timeout)
        return thisWithType()
    }

    override fun shouldBe(vararg condition: Condition?): ThisType {
        self.shouldBe(*condition)
        return thisWithType()
    }

    override fun shouldBe(condition: Condition, timeout: Duration): ThisType {
        self.shouldBe(condition, timeout)
        return thisWithType()
    }

    override fun shouldNot(vararg condition: Condition?): ThisType {
        TODO("Not yet implemented")
    }

    override fun shouldNot(condition: Condition, timeout: Duration): ThisType {
        self.shouldNot(condition, timeout)
        return thisWithType()
    }

    override fun shouldNotHave(vararg condition: Condition?): ThisType {
        self.shouldNotHave(*condition)
        return thisWithType()
    }

    override fun shouldNotHave(condition: Condition, timeout: Duration): ThisType {
        self.shouldNotHave(condition, timeout)
        return thisWithType()
    }

    override fun shouldNotBe(vararg condition: Condition?): ThisType {
        self.shouldNotBe(*condition)
        return thisWithType()
    }

    override fun shouldNotBe(condition: Condition, timeout: Duration): ThisType {
        self.shouldNotBe(condition, timeout)
        return thisWithType()
    }

    override fun `as`(alias: String): ThisType {
        self.`as`(alias)
        return thisWithType()
    }

    override fun scrollTo(): ThisType {
        self.scrollTo()
        return thisWithType()
    }

    override fun scrollIntoView(scrollIntoViewOptions: String): ThisType {
        self.scrollIntoView(scrollIntoViewOptions)
        return thisWithType()
    }

    override fun scrollIntoView(alignToTop: Boolean): ThisType {
        self.scrollIntoView(alignToTop)
        return thisWithType()
    }

    override fun contextClick(): ThisType {
        self.contextClick()
        return thisWithType()
    }

    override fun doubleClick(): ThisType {
        self.doubleClick()
        return thisWithType()
    }

    override fun hover(options: HoverOptions): ThisType {
        self.hover(options)
        return thisWithType()
    }

    override fun hover(): ThisType {
        self.hover()
        return thisWithType()
    }

    override fun dragAndDropTo(targetCssSelector: String, options: DragAndDropOptions): ThisType {
        self.dragAndDropTo(targetCssSelector, options)
        return thisWithType()
    }

    override fun dragAndDropTo(target: WebElement): ThisType {
        self.dragAndDropTo(target)
        return thisWithType()
    }

    override fun dragAndDropTo(targetCssSelector: String): ThisType {
        self.dragAndDropTo(targetCssSelector)
        return thisWithType()
    }
}

/**
 * See
 * https://stackoverflow.com/questions/26992039/what-is-the-proper-way-to-create-new-instance-of-generic-class-in-kotlin
 */
private fun <T : Widget<T>> widgetFactory(element: SelenideElement, factory: (e: SelenideElement) -> T): T {
    return factory(element)
}

/**
 * Same as [ElementsCollection.find], but returns a [Widget] subclass instance instead of [SelenideElement]
 * @sample SampleCode.findWidgetSample
 */
fun <T : Widget<T>> ElementsCollection.findWidget(condition: Condition, factory: (e: SelenideElement) -> T): T {
    return widgetFactory(this.find(condition), factory)
}

/**
 * Same as [ElementsCollection.get], but returns a [Widget] subclass instance instead of [SelenideElement]
 * @sample SampleCode.getWidgetSample
 */
fun <T : Widget<T>> ElementsCollection.getWidget(index: Int, factory: (e: SelenideElement) -> T): T {
    return widgetFactory(this[index], factory)
}

/**
 * Same as [ElementsCollection.first], but returns a [Widget] subclass instance instead of [SelenideElement]
 * @sample SampleCode.firstWidgetSample
 */
fun <T : Widget<T>> ElementsCollection.firstWidget(factory: (e: SelenideElement) -> T): T {
    return widgetFactory(this.first(), factory)
}

/**
 * Same as [ElementsCollection.last], but returns a [Widget] subclass instance instead of [SelenideElement]
 * @sample SampleCode.lastWidgetSample
 */
fun <T : Widget<T>> ElementsCollection.lastWidget(factory: (e: SelenideElement) -> T): T {
    return widgetFactory(this.last(), factory)
}

class SampleCode {
    @Suppress("unused")
    fun newWidgetSubclassSample() {
        class MyWidget(self: SelenideElement) : Widget<MyWidget>(self) {
            val inputField = this.find("input")
            @Required val submit = this.find("button")

            fun pressSubmit() {
                submit.click()
            }
        }
    }

    @Suppress("UNUSED_VARIABLE", "unused")
    fun findWidgetSample() {
        class MyWidget(self: SelenideElement) : Widget<MyWidget>(self)

        val element: MyWidget = Selenide.elements("button").findWidget(Condition.disabled, ::MyWidget)
    }

    @Suppress("UNUSED_VARIABLE", "unused")
    fun getWidgetSample() {
        class MyWidget(self: SelenideElement) : Widget<MyWidget>(self)

        val element: MyWidget = Selenide.elements("button").getWidget(2, ::MyWidget)
    }

    @Suppress("UNUSED_VARIABLE", "unused")
    fun firstWidgetSample() {
        class MyWidget(self: SelenideElement) : Widget<MyWidget>(self)

        val element: MyWidget = Selenide.elements("button").firstWidget(::MyWidget)
    }

    @Suppress("UNUSED_VARIABLE", "unused")
    fun lastWidgetSample() {
        class MyWidget(self: SelenideElement) : Widget<MyWidget>(self)

        val element: MyWidget = Selenide.elements("button").lastWidget(::MyWidget)
    }
}