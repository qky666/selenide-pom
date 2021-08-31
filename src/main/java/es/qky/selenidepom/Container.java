package es.qky.selenidepom;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsContainer;

import javax.annotation.CheckReturnValue;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

@ParametersAreNonnullByDefault
public abstract class Container extends ElementsContainer implements RequiredLoadable {
    @CheckReturnValue
    public final void shouldLoadRequired() {
        this.shouldLoadRequired(Duration.ofMillis(Configuration.timeout));
    }
}
