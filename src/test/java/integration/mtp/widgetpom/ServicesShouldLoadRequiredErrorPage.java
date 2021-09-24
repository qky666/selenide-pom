package integration.mtp.widgetpom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredError;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static com.codeborne.selenide.Condition.text;


@ParametersAreNonnullByDefault
public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {
    @Required @FindBy(css = "div.servicios-principal") public SelenideElement principal;
    @Required @FindBy(css = "h1.h2") public SelenideElement title;

    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        title.shouldHave(text("Aseguramiento de la calidad con error"), timeout);
    }
}
