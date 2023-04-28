package com.github.qky666.selenidepom.pom.common

import com.codeborne.selenide.WebDriverRunner
import com.codeborne.selenide.ex.UIAssertionError

/**
 * Error thrown when a [Required] element is not found, or when [Loadable.customShouldLoadRequired]
 * throws an exception during execution of [shouldLoadRequired].
 *
 * @param errors list of errors found during page load (required elements not found, or custom load validation errors)
 */
class RequiredError(errors: List<Throwable>) : UIAssertionError(
    WebDriverRunner.driver(),
    "Required elements not found in page or screen, or custom page load validation errors found: " +
        "${errors.size} errors. See suppressed errors"
) {
    init {
        errors.forEach { this.addSuppressed(it) }
    }
}
