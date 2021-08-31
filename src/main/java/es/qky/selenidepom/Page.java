package es.qky.selenidepom;

import com.codeborne.selenide.Configuration;

import java.time.Duration;
import javax.annotation.CheckReturnValue;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class Page implements RequiredLoadable {
    @CheckReturnValue
    public final void shouldLoadRequired() {
        this.shouldLoadRequired(Duration.ofMillis(Configuration.timeout));
    }
}
