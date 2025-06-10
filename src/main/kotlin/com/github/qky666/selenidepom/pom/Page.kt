package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.appium.SelenideAppium
import com.codeborne.selenide.appium.SelenideAppiumElement
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import kotlin.reflect.KClass

/**
 * Instances represent a whole web page.
 * A [Page] can contain [com.codeborne.selenide.SelenideElement] and [Widget] that can be annotated as [Required].
 * See [Loadable].
 */
abstract class Page : Loadable {
    companion object {
        private val instances = mutableMapOf<KClass<out Page>, Page>()

        private fun <T : Page> setInstance(page: T): T {
            val klass = page::class
            instances[klass] = page
            return page
        }

        private fun <T : Page> setInstance(klass: KClass<out T>): T =
            setInstance(klass.constructors.first { it.parameters.isEmpty() }.call())

        /**
         * Utility method that allows to get a "default" instance of a [Page].
         * The first time it is called with a particular [KClass],
         * the instance is created using a constructor with no parameters.
         * NOTE: You should not use it with custom [Page] subclasses that do not have a constructor with no parameters.
         *
         * @param klass the [Page] subclass
         * @return the [Page] subclass instance
         */
        fun <T : Page> getInstance(klass: KClass<out T>): T {
            @Suppress("UNCHECKED_CAST")
            return instances[klass] as? T ?: setInstance(klass)
        }

        /**
         * Same as [Selenide.element] `(cssSelector)`.
         *
         * @param cssSelector the CSS selector
         * @return the [SelenideElement] found
         */
        fun find(cssSelector: String) = Selenide.element(cssSelector)

        /**
         * Same as [Selenide.element] `(seleniumSelector)`.
         *
         * @param seleniumSelector the selector
         * @return the [SelenideElement] found
         */
        fun find(seleniumSelector: By) = Selenide.element(seleniumSelector)

        /**
         * Same as [Selenide.element] `(cssSelector, index)`.
         *
         * @param cssSelector the CSS selector
         * @param index the index
         * @return the [SelenideElement] found
         */
        fun find(cssSelector: String, index: Int) = Selenide.element(cssSelector, index)

        /**
         * Same as [Selenide.element] `(seleniumSelector, index)`.
         *
         * @param seleniumSelector the selector
         * @param index the index
         * @return the [SelenideElement] found
         */
        fun find(seleniumSelector: By, index: Int) = Selenide.element(seleniumSelector, index)

        /**
         * Same as [Selenide.element] `(webElement)`.
         *
         * @param webElement the element
         * @return the wrapped [SelenideElement]
         */
        fun find(webElement: WebElement) = Selenide.element(webElement)

        /**
         * Same as [Selenide.element] `(By.xpath(xpathExpression))`.
         *
         * @param xpathExpression the xpath
         * @return the [SelenideElement] found
         */
        fun findX(xpathExpression: String) = Selenide.element(By.xpath(xpathExpression))

        /**
         * Same as [Selenide.element] `(By.xpath(xpathExpression), index)`.
         *
         * @param xpathExpression the xpath
         * @param index the index
         * @return the unique [SelenideElement] found
         */
        fun findX(xpathExpression: String, index: Int) = Selenide.element(By.xpath(xpathExpression), index)

        /**
         * Same as [Selenide.elements] `(cssSelector)`.
         *
         * @param cssSelector the CSS selector
         * @return the [ElementsCollection] found
         */
        fun findAll(cssSelector: String) = Selenide.elements(cssSelector)

        /**
         * Same as [Selenide.elements] `(seleniumSelector)`.
         *
         * @param seleniumSelector the selector
         * @return the [ElementsCollection] found
         */
        fun findAll(seleniumSelector: By) = Selenide.elements(seleniumSelector)

        /**
         * Same as [Selenide.elements] `(elements)`.
         *
         * @param elements the element collection
         * @return the wrapped [ElementsCollection]
         */
        fun findAll(elements: Collection<WebElement>) = Selenide.elements(elements)

        /**
         * Same as [Selenide.elements] `(By.xpath(xpathExpression))`.
         *
         * @param xpathExpression the xpath
         * @return the [ElementsCollection] found
         */
        fun findXAll(xpathExpression: String) = Selenide.elements(By.xpath(xpathExpression))

        /**
         * Same as [SelenideAppium]`.$ (seleniumSelector)`.
         *
         * @param seleniumSelector the selector
         * @return the [SelenideAppiumElement] found
         */
        @Suppress("unused")
        fun findAppium(seleniumSelector: By) = SelenideAppium.`$`(seleniumSelector)

        /**
         * Same as [SelenideAppium]`.$ (seleniumSelector, index)`.
         *
         * @param seleniumSelector the selector
         * @param index the index
         * @return the [SelenideAppiumElement] found
         */
        @Suppress("unused")
        fun findAppium(seleniumSelector: By, index: Int) = SelenideAppium.`$`(seleniumSelector, index)

        /**
         * Same as [SelenideAppium]`.$x (xpathExpression)`.
         *
         * @param xpathExpression the xpath
         * @return the [SelenideAppiumElement] found
         */
        @Suppress("unused")
        fun findAppium(xpathExpression: String) = SelenideAppium.`$x`(xpathExpression)

        /**
         * Same as [SelenideAppium]`.$ (By.xpath(xpathExpression), index)`.
         *
         * @param xpathExpression the xpath
         * @return the [SelenideAppiumElement] found
         */
        @Suppress("unused")
        fun findAppium(xpathExpression: String, index: Int) = SelenideAppium.`$`(By.xpath(xpathExpression), index)
    }
}
