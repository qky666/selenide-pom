package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.*
import org.openqa.selenium.WebElement
import java.time.Duration

/**
 * Abstract class that implements [SelenideElement] and [Loadable] interfaces.
 */
abstract class Widget<ThisType : Widget<ThisType>> (private val self: SelenideElement) : Loadable, SelenideElement by self {

    private fun thisWithType(): ThisType {
        @Suppress("UNCHECKED_CAST")
        return this as ThisType
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
        return thisWithType()    }

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
