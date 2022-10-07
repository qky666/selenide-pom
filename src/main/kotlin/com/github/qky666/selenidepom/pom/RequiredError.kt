package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.ex.UIAssertionError

/**
 * Error thrown when a [com.github.qky666.selenidepom.pom.Required] element is not found,
 * or when [com.github.qky666.selenidepom.pom.Loadable.customShouldLoadRequired] throws an exception during execution of
 * [com.github.qky666.selenidepom.pom.shouldLoadRequired].
 *
 * @param errors List of errors found during page load (required elements not found, or custom load validation errors)
 */
class RequiredError(errors: List<Throwable>) : UIAssertionError(
    "Required elements not found in page, or custom page load validation errors found: ${errors.size} errors. See suppressed errors"
) {
    init {
        errors.forEach { this.addSuppressed(it) }
    }
}
