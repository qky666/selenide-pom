package integration.mtp.deeppom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.kotlin.Required;
import com.github.qky666.selenidepom.kotlin.RequiredError;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class ServicesPage extends MainFramePage {
    @Required
    @FindBy(css = "div.servicios-principal")
    public SelenideElement principal;
    @Required
    @FindBy(css = "h1.h2")
    public SelenideElement title;
    @FindBy(css = "bad-selector")
    public SelenideElement badSelector;

    @Override
    public void shouldLoadRequiredWithTimeout(Duration timeout) throws RequiredError {
        super.shouldLoadRequiredWithTimeout(timeout);
        title.shouldHave(Condition.text("Aseguramiento de la calidad"), timeout);
    }
}
