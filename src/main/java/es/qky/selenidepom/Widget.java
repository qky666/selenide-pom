package es.qky.selenidepom;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsContainer;

import javax.annotation.CheckReturnValue;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

/**
 * Page Object Model's Widget superclass with @Required support.
 */
@ParametersAreNonnullByDefault
public abstract class Widget extends ElementsContainer implements RequiredLoadable {
    /**
     * All fields with @Required annotation are checked if visible, with default timeout (Selenide Configuration).
     */
    @CheckReturnValue
    public final void shouldLoadRequired() {
        this.shouldLoadRequired(Duration.ofMillis(Configuration.timeout));
    }
}
