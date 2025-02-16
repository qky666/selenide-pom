package com.github.qky666.selenidepom.pom

import org.openqa.selenium.Rectangle
import kotlin.io.path.toPath

class ImageHelper {
    companion object {
        fun pathToImage(image: String): String {
            val relativePath = "images/$image"
            return Thread.currentThread().contextClassLoader.getResource(relativePath)?.toURI()?.toPath().toString()
        }
    }
}

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
