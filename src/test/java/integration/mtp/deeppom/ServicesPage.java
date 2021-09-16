package integration.mtp.deeppom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;


@ParametersAreNonnullByDefault
public class ServicesPage extends MainFramePage {
    @Required @FindBy(css = "div.servicios-principal") public SelenideElement principal;
    @Required @FindBy(css = "h1.h2") public SelenideElement titleTxt;
    @FindBy(css = "bad-selector") public SelenideElement badSelector;

    @Override
    public void shouldLoadRequired(Duration timeout) {
        super.shouldLoadRequired(timeout);
        titleTxt.shouldHave(Condition.text("Aseguramiento de la calidad"), timeout);
    }
}
