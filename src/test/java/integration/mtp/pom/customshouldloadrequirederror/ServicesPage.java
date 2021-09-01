package integration.mtp.pom.customshouldloadrequirederror;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class ServicesPage extends CommonFramePage {
    @Required
    public SelenideElement principal = $("div.servicios-principal");
    @Required
    public final SelenideElement titleTxt = $("h1.h2");

    @Override
    public void shouldLoadRequired(Duration timeout) {
        super.shouldLoadRequired(timeout);
        titleTxt.shouldHave(text("Aseguramiento de la calidad error"), timeout);
    }
}
