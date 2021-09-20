package integration.mtp.deeppom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;


@ParametersAreNonnullByDefault
public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {
    @Required @FindBy(css = "div.servicios-principal") public SelenideElement principal;
    @Required @FindBy(css = "h1.h2") public SelenideElement titleTxt;

    @Override
    public void shouldLoadRequired(Duration timeout) throws Throwable {
        super.shouldLoadRequired(timeout);
        titleTxt.shouldHave(Condition.text("Aseguramiento de la calidad con error"), timeout);
    }
}
