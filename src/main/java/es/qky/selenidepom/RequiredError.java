package es.qky.selenidepom;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.UIAssertionError;

import java.util.ArrayList;
import java.util.List;


/**
 * Error thrown when a @Required element is not found.
 */
public class RequiredError extends UIAssertionError {
    final List<Throwable> errors;

    /**
     * Constructor.
     * @param errors List of Throwable errors found while looking for @Required elements.
     */
    public RequiredError(List<Throwable> errors) {
        super(WebDriverRunner.driver(), "Required elements not found in page: " + errors.size() + " errors. See suppressed errors");
        this.errors = errors;
        errors.forEach(this::addSuppressed);
    }

    /**
     * Accessor.
     * @return errors list.
     */
    public List<Throwable> getErrors() {
        return new ArrayList<>(errors);
    }
}
