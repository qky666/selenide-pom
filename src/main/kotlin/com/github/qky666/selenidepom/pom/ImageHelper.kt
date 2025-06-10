package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.ClickMethod
import com.codeborne.selenide.ClickOptions
import io.github.oshai.kotlinlogging.KotlinLogging
import org.bytedeco.javacpp.indexer.FloatIndexer
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.Point as CVPoint
import org.openqa.selenium.Rectangle

private val logger = KotlinLogging.logger {}

/**
 * Returns `true` if [this] [Rectangle] is contained in [other] [Rectangle], `false` otherwise.
 *
 * @param other the other [Rectangle]
 * @return `true` if [this] is contained in [other], `false` otherwise.
 */
fun Rectangle.isContainedIn(other: Rectangle) = if (this.x < other.x) false
else if (this.y < other.y) false
else if (this.x + this.width > other.x + other.width) false
else if (this.y + this.height > other.y + other.height) false
else true

/**
 * Returns `true` if [this] [Rectangle] contains [other] [Rectangle], `false` otherwise.
 *
 * @param other the other [Rectangle]
 * @return `true` if [this] contains [other], `false` otherwise.
 */
fun Rectangle.contains(other: Rectangle) = other.isContainedIn(this)

/**
 * Returns the first point above the given threshold in a [Mat]
 *
 * @param m the [Mat]
 * @param t the threshold
 * @return the point found, or `null` if there are no points above given threshold
 */
fun getFirstPointFromMatAboveThreshold(m: Mat, t: Float): CVPoint? {
    logger.debug { "Starting getFirstPointFromMatAboveThreshold. Target threshold: $t" }
    val indexer = m.createIndexer<FloatIndexer>()
    for (y in 0 until m.rows()) {
        for (x in 0 until m.cols()) {
            val similarity = indexer[y.toLong(), x.toLong()]
            if (similarity > t) return CVPoint(x, y)
        }
    }
    logger.debug { "getFirstPointFromMatAboveThreshold. No point above threshold $t found" }
    return null
}

/**
 * Returns all points above the given threshold in a [Mat]
 *
 * @param m the [Mat]
 * @param t the threshold
 * @return the points found
 */
fun getPointsFromMatAboveThreshold(m: Mat, t: Float): List<CVPoint> {
    logger.debug { "Starting getPointsFromMatAboveThreshold. Target threshold: $t" }
    val matches = mutableListOf<CVPoint>()
    val indexer = m.createIndexer<FloatIndexer>()
    for (y in 0 until m.rows()) {
        for (x in 0 until m.cols()) {
            val similarity = indexer[y.toLong(), x.toLong()]
            if (similarity > t) matches.add(CVPoint(x, y))
        }
    }
    logger.debug { "getPointsFromMatAboveThreshold. Points above threshold $t found: ${matches.size}" }
    return matches
}

/**
 * Creates a new [ClickOptions] object creating a [clickOption] copy, but with offset modified:
 * - new `offsetX` = old `offsetX` + [offsetCorrectionX]
 * - new `offsetY` = old `offsetY` + [offsetCorrectionY]
 *
 * @param clickOption initial [ClickOptions] object
 * @param offsetCorrectionX amount added to `offsetX` in final [ClickOptions] object
 * @param offsetCorrectionY amount added to `offsetY` in final [ClickOptions] object
 * @return created [ClickOptions] object with corrected offset
 */
fun correctClickOptionOffset(
    clickOption: ClickOptions,
    offsetCorrectionX: Int,
    offsetCorrectionY: Int,
): ClickOptions {
    val basicOption = when (clickOption.clickMethod()) {
        ClickMethod.DEFAULT -> ClickOptions.usingDefaultMethod()
        ClickMethod.JS -> ClickOptions.usingJavaScript()
    }.offsetX(clickOption.offsetX() + offsetCorrectionX).offsetY(clickOption.offsetY() + offsetCorrectionY)
    val timeoutOption = clickOption.timeout()?.let { basicOption.timeout(it) } ?: basicOption
    val option = if (clickOption.isForce) timeoutOption.force() else timeoutOption
    return option
}
