package es.qky.selenidepom;

import com.codeborne.selenide.Configuration;

import java.time.Duration;
import javax.annotation.CheckReturnValue;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Page Object Model's Page superclass with @Required support.
 */
@ParametersAreNonnullByDefault
public abstract class Page implements RequiredLoadable {
    /**
     * All fields with @Required annotation are checked if visible, with default timeout (Selenide Configuration).
     */
    @CheckReturnValue
    public final void shouldLoadRequired() {
        this.shouldLoadRequired(Duration.ofMillis(Configuration.timeout));
    }
}
