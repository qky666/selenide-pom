package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebElementCondition
import java.time.Duration

/**
 * Same as [SelenideElement.has] (condition: Condition), but waits until condition is true, or timeout is reached.
 *
 * @param condition the [Condition]
 * @param timeout the maximum waiting time until condition is true
 * @return true if the element matches given [Condition], false if not
 */
fun SelenideElement.has(condition: WebElementCondition, timeout: Duration): Boolean {
    try {
        this.should(condition, timeout)
    } catch (_: Throwable) {
    }
    return this.has(condition)
}

/**
 * Same as [SelenideElement.scrollIntoView] ("{behavior: \"auto\", block: \"center\", inline: \"center\"}")
 *
 * @return this
 */
fun <T : SelenideElement> T.scrollToCenter(): T {
    this.scrollIntoView("{behavior: \"auto\", block: \"center\", inline: \"center\"}")
    return this
}
