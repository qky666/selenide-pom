package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.appium.SelenideAppium
import com.codeborne.selenide.appium.SelenideAppiumElement
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

/**
 * Instances represent a whole web page.
 * A [Page] can contain [com.codeborne.selenide.SelenideElement] and [Widget] that can be annotated as [Required].
 * See [Loadable].
 */
abstract class Page : Loadable {
    companion object {
        /**
         * Same as [Selenide.element] `(cssSelector)`.
         *
         * @param cssSelector the css selector
         * @return the [SelenideElement] found
         */
        fun find(cssSelector: String): SelenideElement {
            return Selenide.element(cssSelector)
        }

        /**
         * Same as [Selenide.element] `(seleniumSelector)`.
         *
         * @param seleniumSelector the selector
         * @return the [SelenideElement] found
         */
        fun find(seleniumSelector: By): SelenideElement {
            return Selenide.element(seleniumSelector)
        }

        /**
         * Same as [Selenide.element] `(cssSelector, index)`.
         *
         * @param cssSelector the css selector
         * @param index the index
         * @return the [SelenideElement] found
         */
        fun find(cssSelector: String, index: Int): SelenideElement {
            return Selenide.element(cssSelector, index)
        }

        /**
         * Same as [Selenide.element] `(seleniumSelector, index)`.
         *
         * @param seleniumSelector the selector
         * @param index the index
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
         * Same as [Selenide.element] `(By.xpath(xpathExpression))`.
         *
         * @param xpathExpression the xpath
         * @return the [SelenideElement] found
         */
        fun findX(xpathExpression: String): SelenideElement {
            return Selenide.element(By.xpath(xpathExpression))
        }

        /**
         * Same as [Selenide.element] `(By.xpath(xpathExpression), index)`.
         *
         * @param xpathExpression the xpath
         * @param index the index
         * @return the unique [SelenideElement] found
         */
        fun findX(xpathExpression: String, index: Int): SelenideElement {
            return Selenide.element(By.xpath(xpathExpression), index)
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
         * Same as [Selenide.elements] `(seleniumSelector)`.
         *
         * @param seleniumSelector the selector
         * @return the [ElementsCollection] found
         */
        fun findAll(seleniumSelector: By): ElementsCollection {
            return Selenide.elements(seleniumSelector)
        }

        /**
         * Same as [Selenide.elements] `(elements)`.
         *
         * @param elements the element collection
         * @return the wrapped [ElementsCollection]
         */
        fun findAll(elements: Collection<WebElement>): ElementsCollection {
            return Selenide.elements(elements)
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
         * Same as [SelenideAppium]`.$ (seleniumSelector)`.
         *
         * @param seleniumSelector the selector
         * @return the [SelenideAppiumElement] found
         */
        @Suppress("unused")
        fun findAppium(seleniumSelector: By): SelenideAppiumElement {
            return SelenideAppium.`$`(seleniumSelector)
        }

        /**
         * Same as [SelenideAppium]`.$ (seleniumSelector, index)`.
         *
         * @param seleniumSelector the selector
         * @param index the index
         * @return the [SelenideAppiumElement] found
         */
        @Suppress("unused")
        fun findAppium(seleniumSelector: By, index: Int): SelenideAppiumElement {
            return SelenideAppium.`$`(seleniumSelector, index)
        }

        /**
         * Same as [SelenideAppium]`.$x (xpathExpression)`.
         *
         * @param xpathExpression the xpath
         * @return the [SelenideAppiumElement] found
         */
        @Suppress("unused")
        fun findAppium(xpathExpression: String): SelenideAppiumElement {
            return SelenideAppium.`$x`(xpathExpression)
        }
    }
}
