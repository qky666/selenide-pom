package com.github.qky666.selenidepom.java;

import com.codeborne.selenide.ex.UIAssertionError;

import javax.annotation.CheckReturnValue;
import java.util.List;

/**
 * Error thrown when a {@link Required} element is not found.
 */
public class RequiredError extends UIAssertionError {
    /**
     * Constructor.
     *
     * @param errors List of Throwable errors found while looking for {@link Required} elements.
     */
    @CheckReturnValue
    public RequiredError(List<Throwable> errors) {
        super("Required elements not found in page: " + errors.size() + " errors. See suppressed errors");
        errors.forEach(this::addSuppressed);
    }
}
