package com.github.qky666.selenidepom.pom

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
    fun find(cssSelector: String): SelenideElement {
        return Selenide.element(cssSelector)
    }

    fun findX(xpathExpression: String): SelenideElement {
        return Selenide.element(By.xpath(xpathExpression))
    }

    fun find(cssSelector: String, index: Int): SelenideElement {
        return Selenide.element(cssSelector, index)
    }

    fun findX(xpathExpression: String, index: Int): SelenideElement {
        return Selenide.element(By.xpath(xpathExpression), index)
    }

    fun find(seleniumSelector: By): SelenideElement {
        return Selenide.element(seleniumSelector)
    }

    fun find(seleniumSelector: By, index: Int): SelenideElement {
        return Selenide.element(seleniumSelector, index)
    }

    fun find(webElement: WebElement): SelenideElement {
        return Selenide.element(webElement)
    }

    fun findAll(cssSelector: String): ElementsCollection {
        return Selenide.elements(cssSelector)
    }

    fun findXAll(xpathExpression: String): ElementsCollection {
        return Selenide.elements(By.xpath(xpathExpression))
    }

    fun findAll(seleniumSelector: By): ElementsCollection {
        return Selenide.elements(seleniumSelector)
    }

    fun findAll(elements: Collection<WebElement>): ElementsCollection {
        return Selenide.elements(elements)
    }
}
