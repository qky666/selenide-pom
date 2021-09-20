package integration.mtp.simplepom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;


@ParametersAreNonnullByDefault
public class ServicesRequiredErrorPage extends MainFramePage {
    @Required public SelenideElement principal = $("div.servicios-principal");
    @Required public final SelenideElement titleTxt = $("h1.h2");
    // badSelector generates an error in shouldLoadRequired
    @Required public final SelenideElement badSelector = $("bad-selector");


    @Override
    public void shouldLoadRequired(Duration timeout) throws Throwable {
        super.shouldLoadRequired(timeout);
        titleTxt.shouldHave(Condition.text("Aseguramiento de la calidad"), timeout);
    }
}
