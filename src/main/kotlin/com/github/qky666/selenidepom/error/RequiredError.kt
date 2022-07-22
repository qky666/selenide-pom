package com.github.qky666.selenidepom.error

import com.codeborne.selenide.ex.UIAssertionError

/**
 * Error thrown when a [com.github.qky666.selenidepom.annotation.Required] element is not found.
 */
class RequiredError(errors: List<Throwable>) :
    UIAssertionError("Required elements not found in page: ${errors.size} errors. See suppressed errors") {
    init {
        errors.forEach { this.addSuppressed(it) }
    }
}
