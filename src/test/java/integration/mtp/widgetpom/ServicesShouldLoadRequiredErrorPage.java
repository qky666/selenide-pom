package integration.mtp.widgetpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.kotlin.Required;
import com.github.qky666.selenidepom.kotlin.RequiredError;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;

public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {
    @Required
    @FindBy(css = "div.servicios-principal")
    public SelenideElement principal;
    @Required
    @FindBy(css = "h1.h2")
    public SelenideElement title;

    @Override
    public void shouldLoadRequiredWithTimeout(Duration timeout) throws RequiredError {
        super.shouldLoadRequiredWithTimeout(timeout);
        title.shouldHave(text("Aseguramiento de la calidad con error"), timeout);
    }
}
