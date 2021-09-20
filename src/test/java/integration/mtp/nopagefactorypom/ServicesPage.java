package integration.mtp.nopagefactorypom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;


@ParametersAreNonnullByDefault
public class ServicesPage extends MainFramePage {
    @Required public final SelenideElement principal = $("div.servicios-principal");
    @Required public final SelenideElement titleTxt = $("h1.h2");
    public final SelenideElement badSelector = $("bad-selector");

    @Override
    public void shouldLoadRequired(Duration timeout) throws Throwable {
        super.shouldLoadRequired(timeout);
        titleTxt.shouldHave(text("Aseguramiento de la calidad"), timeout);
    }
}