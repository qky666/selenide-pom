package com.github.qky666.selenidepom.pom.mobile

import com.codeborne.selenide.appium.SelenideAppium
import com.codeborne.selenide.appium.SelenideAppiumElement
import com.github.qky666.selenidepom.pom.common.Loadable
import org.openqa.selenium.By

/**
 * Instances represent a whole screen in a mobile app.
 * A [Screen] can contain [SelenideAppiumElement] that can be annotated as
 * [com.github.qky666.selenidepom.pom.common.Required].
 * See [Loadable].
 */
abstract class Screen : Loadable {
    companion object {
        /**
         * Same as [SelenideAppium]`.$ (seleniumSelector)`.
         *
         * @param seleniumSelector the selector
         * @return the [SelenideAppiumElement] found
         */
        fun find(seleniumSelector: By): SelenideAppiumElement {
            return SelenideAppium.`$`(seleniumSelector)
        }

        /**
         * Same as [SelenideAppium]`.$ (seleniumSelector, index)`.
         *
         * @param seleniumSelector the selector
         * @return the [SelenideAppiumElement] found
         */
        fun find(seleniumSelector: By, index: Int): SelenideAppiumElement {
            return SelenideAppium.`$`(seleniumSelector, index)
        }

        /**
         * Same as [SelenideAppium]`.$x`.
         *
         * @param xpathExpression the xpath
         * @return the [SelenideAppiumElement] found
         */
        fun findX(xpathExpression: String): SelenideAppiumElement {
            return SelenideAppium.`$x`(xpathExpression)
        }
    }
}
