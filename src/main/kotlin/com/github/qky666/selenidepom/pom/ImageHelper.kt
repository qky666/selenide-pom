package com.github.qky666.selenidepom.pom

import org.openqa.selenium.Rectangle

/**
 * Returns `true` if [this] [Rectangle] is contained in [other] [Rectangle], `false` otherwise.
 *
 * @param other the other [Rectangle]
 * @return `true` if [this] is contained in [other], `false` otherwise.
 */
fun Rectangle.isContainedIn(other: Rectangle): Boolean {
    if (this.x < other.x) return false
    if (this.y < other.y) return false
    if (this.x + this.width > other.x + other.width) return false
    if (this.y + this.height > other.y + other.height) return false
    return true
}

/**
 * Returns `true` if [this] [Rectangle] contains [other] [Rectangle], `false` otherwise.
 *
 * @param other the other [Rectangle]
 * @return `true` if [this] contains [other], `false` otherwise.
 */
fun Rectangle.contains(other: Rectangle): Boolean {
    return other.isContainedIn(this)
}
