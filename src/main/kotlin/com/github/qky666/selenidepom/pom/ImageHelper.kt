package com.github.qky666.selenidepom.pom

import org.bytedeco.javacpp.indexer.FloatIndexer
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.Point as CVPoint
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

/**
 * Returns the first point above given threshold in a [Mat]
 *
 * @param m the [Mat]
 * @param t the threshold
 * @return the point found, or `null` if there are no points above given threshold
 */
fun getFirstPointFromMatAboveThreshold(m: Mat, t: Float): CVPoint? {
    val indexer = m.createIndexer<FloatIndexer>()
    for (y in 0..<m.rows()) {
        for (x in 0..<m.cols()) {
            if (indexer[y.toLong(), x.toLong()] > t) return CVPoint(x, y)
        }
    }
    return null
}

/**
 * Returns all points above given threshold in a [Mat]
 *
 * @param m the [Mat]
 * @param t the threshold
 * @return the points found
 */
fun getPointsFromMatAboveThreshold(m: Mat, t: Float): List<CVPoint> {
    val matches = mutableListOf<CVPoint>()
    val indexer = m.createIndexer<FloatIndexer>()
    for (y in 0..<m.rows()) {
        for (x in 0..<m.cols()) {
            if (indexer[y.toLong(), x.toLong()] > t) matches.add(CVPoint(x, y))
        }
    }
    return matches
}