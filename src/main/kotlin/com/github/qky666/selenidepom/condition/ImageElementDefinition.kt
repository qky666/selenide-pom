package com.github.qky666.selenidepom.condition

import java.nio.file.Path

/**
 * Represents a unique image (stored as a [Path]) used to search in a web page screenshot.
 * Properties [enabled] and [selected] set if the found web element is considered to be enabled or selected.
 *
 * @param path path to the image
 * @param enabled sets if the found web element using this image is considered to be enabled. Default value: true
 * @param selected sets if the found web element using this image is considered to be selected. Default value: false
 */
data class ImageElementDefinition(val path: Path, val enabled: Boolean = true, val selected: Boolean = false)
