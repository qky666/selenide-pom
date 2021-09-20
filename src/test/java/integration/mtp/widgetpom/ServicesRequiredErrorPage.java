package integration.mtp.widgetpom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static com.codeborne.selenide.Condition.text;


@ParametersAreNonnullByDefault
public class ServicesRequiredErrorPage extends MainFramePage {
    @Required @FindBy(css = "div.servicios-principal") public SelenideElement principal;
    @Required @FindBy(css = "h1.h2") public SelenideElement titleTxt;
    // badSelector generates an error in shouldLoadRequired
    @Required @FindBy(css = "bad-selector") public SelenideElement badSelector;

    @Override
    public void shouldLoadRequired(Duration timeout) throws Throwable {
        super.shouldLoadRequired(timeout);
        titleTxt.shouldHave(text("Aseguramiento de la calidad"));
    }
}
