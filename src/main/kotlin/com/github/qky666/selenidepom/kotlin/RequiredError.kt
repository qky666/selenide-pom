package com.github.qky666.selenidepom.kotlin

import com.codeborne.selenide.ex.UIAssertionError

/**
 * Error thrown when a [Required] element is not found.
 */
class RequiredError(errors: List<Throwable>) :
    UIAssertionError("Required elements not found in page: ${errors.size} errors. See suppressed errors") {
    init {
        errors.forEach { this.addSuppressed(it) }
    }
}
