package integration.mtp.simplepom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.RequiredError;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class ServicesRequiredErrorPage extends MainFramePage {
    @Required
    public SelenideElement principal = $("div.servicios-principal");
    @Required
    public final SelenideElement title = $("h1.h2");
    // badSelector generates an error in shouldLoadRequired
    @Required
    public final SelenideElement badSelector = $("bad-selector");


    @Override
    public void shouldLoadRequiredWithTimeout(Duration timeout) throws RequiredError {
        super.shouldLoadRequiredWithTimeout(timeout);
        title.shouldHave(Condition.text("Aseguramiento de la calidad"), timeout);
    }
}
