package integration.mtp.lazypom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.kotlin.Required;
import com.github.qky666.selenidepom.kotlin.RequiredError;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ServicesPage extends MainFramePage {
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement principal = $("div.servicios-principal");
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement title = $("h1.h2");
    @Getter(lazy = true)
    private final SelenideElement badSelector = $("bad-selector");

    @Override
    public void shouldLoadRequiredWithTimeout(Duration timeout) throws RequiredError {
        super.shouldLoadRequiredWithTimeout(timeout);
        getTitle().shouldHave(text("Aseguramiento de la calidad"), timeout);
    }
}
