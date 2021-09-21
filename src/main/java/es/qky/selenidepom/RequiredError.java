package es.qky.selenidepom;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.UIAssertionError;

import java.util.ArrayList;


public class RequiredError extends UIAssertionError {
    final ArrayList<Throwable> errors;

    public RequiredError(ArrayList<Throwable> errors) {
        super(WebDriverRunner.driver(), "Required elements not found in page. See suppressed errors");
        this.errors = errors;
        for (Throwable error : errors) {
            addSuppressed(error);
        }
    }

    public ArrayList<Throwable> getErrors() {
        return new ArrayList<>(errors);
    }
}
