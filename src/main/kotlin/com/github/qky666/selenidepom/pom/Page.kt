package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * Instances represent a whole web page.
 * A [Page] can contain [com.codeborne.selenide.SelenideElement] and [Widget] that can be annotated as [Required].
 * See [Loadable].
 */
abstract class Page : Loadable {
    companion object{
        /**
         * Same as [Selenide.element] `(cssSelector)`, but it assures that the [cssSelector] provided returns
         * a unique element to avoid errors.
         * An exception is thrown if the provided selector returns no elements, or more than one element.
         * If more than one element is returned, you can always use the `find(selector, index)` method
         * or use a more specific [cssSelector].
         *
         * @param cssSelector the css selector
         * @return the unique [SelenideElement] found
         */
        fun find(cssSelector: String): SelenideElement {
            return Selenide.elements(cssSelector).shouldHave(size(1))[0]
        }

        /**
         * Same as [Selenide.element] `(By.xpath(xpathExpression))`, but it assures that the [xpathExpression] provided returns
         * a unique element to avoid errors.
         * An exception is thrown if the provided selector returns no elements, or more than one element.
         * If more than one element is returned, you can always use the `find(selector, index)` method
         * or use a more specific [xpathExpression].
         *
         * @param xpathExpression the xpath
         * @return the unique [SelenideElement] found
         */
        fun findX(xpathExpression: String): SelenideElement {
            return Selenide.elements(By.xpath(xpathExpression)).shouldHave(size(1))[0]
        }

        /**
         * Same as [Selenide.element] `(cssSelector, index)`.
         *
         * @param cssSelector the css selector
         * @return the [SelenideElement] found
         */
        fun find(cssSelector: String, index: Int): SelenideElement {
            return Selenide.element(cssSelector, index)
        }

        /**
         * Same as [Selenide.element] `(By.xpath(xpathExpression), index)`.
         *
         * @param xpathExpression the xpath
         * @return the unique [SelenideElement] found
         */
        fun findX(xpathExpression: String, index: Int): SelenideElement {
            return Selenide.element(By.xpath(xpathExpression), index)
        }

        /**
         * Same as [Selenide.element] `(seleniumSelector)`, but it assures that the [seleniumSelector] provided returns
         * a unique element to avoid errors.
         * An exception is thrown if the provided selector returns no elements, or more than one element.
         * If more than one element is returned, you can always use the `find(selector, index)` method
         * or use a more specific [seleniumSelector].
         *
         * @param seleniumSelector the selector
         * @return the unique [SelenideElement] found
         */
        fun find(seleniumSelector: By): SelenideElement {
            return Selenide.elements(seleniumSelector).shouldHave(size(1))[0]
        }

        /**
         * Same as [Selenide.element] `(seleniumSelector, index)`.
         *
         * @param seleniumSelector the selector
         * @return the [SelenideElement] found
         */
        fun find(seleniumSelector: By, index: Int): SelenideElement {
            return Selenide.element(seleniumSelector, index)
        }

        /**
         * Same as [Selenide.element] `(webElement)`.
         *
         * @param webElement the element
         * @return the wrapped [SelenideElement]
         */
        fun find(webElement: WebElement): SelenideElement {
            return Selenide.element(webElement)
        }

        /**
         * Same as [Selenide.elements] `(cssSelector)`.
         *
         * @param cssSelector the css selector
         * @return the [ElementsCollection] found
         */
        fun findAll(cssSelector: String): ElementsCollection {
            return Selenide.elements(cssSelector)
        }

        /**
         * Same as [Selenide.elements] `(By.xpath(xpathExpression))`.
         *
         * @param xpathExpression the xpath
         * @return the [ElementsCollection] found
         */
        fun findXAll(xpathExpression: String): ElementsCollection {
            return Selenide.elements(By.xpath(xpathExpression))
        }

        /**
         * Same as [Selenide.elements] `(seleniumSelector)`.
         *
         * @param seleniumSelector the selector
         * @return the [ElementsCollection] found
         */
        fun findAll(seleniumSelector: By): ElementsCollection {
            return Selenide.elements(seleniumSelector)
        }

        /**
         * Same as [Selenide.element] `(elements)`.
         *
         * @param elements the element collection
         * @return the wrapped [ElementsCollection]
         */
        fun findAll(elements: Collection<WebElement>): ElementsCollection {
            return Selenide.elements(elements)
        }
    }
}
