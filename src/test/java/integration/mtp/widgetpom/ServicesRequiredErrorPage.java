package integration.mtp.widgetpom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredError;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;

public class ServicesRequiredErrorPage extends MainFramePage {
    @Required
    @FindBy(css = "div.servicios-principal")
    public SelenideElement principal;
    @Required
    @FindBy(css = "h1.h2")
    public SelenideElement title;
    // badSelector generates an error in shouldLoadRequired
    @Required
    @FindBy(css = "bad-selector")
    public SelenideElement badSelector;
    @Required
    @FindBy(css = "other-bad-selector")
    public SelenideElement otherBadSelector;

    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        title.shouldHave(text("Aseguramiento de la calidad"));
    }
}
