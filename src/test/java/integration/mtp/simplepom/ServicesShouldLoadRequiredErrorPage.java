package integration.mtp.simplepom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredError;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {
    @Required
    public SelenideElement principal = $("div.servicios-principal");
    @Required
    public final SelenideElement title = $("h1.h2");

    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        title.shouldHave(Condition.text("Aseguramiento de la calidad con error"), timeout);
    }
}
