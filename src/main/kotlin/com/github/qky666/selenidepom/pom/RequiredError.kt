package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.ex.UIAssertionError

/**
 * Error thrown when a [Required] element is not found, or when [Loadable.customShouldLoadRequired]
 * throws an exception during execution of [shouldLoadRequired].
 *
 * @param errors list of errors found during the page load (required elements not found, or custom load validation errors)
 */
class RequiredError(private val errors: List<Throwable>) : UIAssertionError(
    "Required elements not found in page or screen, or custom page load validation errors found: " +
            "${errors.size} errors. See suppressed errors"
) {
    init {
        errors.forEach { this.addSuppressed(it) }
    }
}
