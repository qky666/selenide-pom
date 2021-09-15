package integration.mtp.pom.customshouldloadrequirederrorpom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static com.codeborne.selenide.Condition.text;


@ParametersAreNonnullByDefault
public class ServicesPage extends CommonFramePage {
    @Required @FindBy(css = "div.servicios-principal") public SelenideElement principal;
    @Required @FindBy(css = "h1.h2") public SelenideElement titleTxt;

    @Override
    public void shouldLoadRequired(Duration timeout) {
        super.shouldLoadRequired(timeout);
        titleTxt.shouldHave(text("Aseguramiento de la calidad error"), timeout);
    }
}
