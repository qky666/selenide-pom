package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.SelenideElement

/**
 * Same as [SelenideElement.scrollIntoView] ("{behavior: \"auto\", block: \"center\", inline: \"center\"}")
 *
 * @return `this`
 */
fun <T : SelenideElement> T.scrollToCenter(): T {
    this.scrollIntoView("{behavior: \"auto\", block: \"center\", inline: \"center\"}")
    return this
}
