package integration.mtp.lazypom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredError;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ServicesRequiredErrorPage extends MainFramePage {
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement principal = $("div.servicios-principal");
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement title = $("h1.h2");
    // badSelector generates an error in shouldLoadRequired
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement badSelector = $("bad-selector");
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement otherBadSelector = $("other-bad-selector");

    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        getTitle().shouldHave(text("Aseguramiento de la calidad"));
    }
}
