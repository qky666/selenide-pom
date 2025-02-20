package com.github.qky666.selenidepom.pom

import org.openqa.selenium.Rectangle


fun Rectangle.isContainedIn(other: Rectangle): Boolean {
    if (this.x < other.x) return false
    if (this.y < other.y) return false
    if (this.x + this.width > other.x + other.width) return false
    if (this.y + this.height > other.y + other.height) return false
    return true
}

fun Rectangle.contains(other: Rectangle): Boolean {
    return other.isContainedIn(this)
}
