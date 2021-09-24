package integration.mtp.widgetpom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredError;
import es.qky.selenidepom.Widget;
import org.openqa.selenium.support.FindBy;

import javax.annotation.CheckReturnValue;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;


@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CookiesBanner extends Widget {
    @Required @FindBy(css = "span#cn-notice-text") public SelenideElement cookiesText;
    @Required @FindBy(css = "a#cn-accept-cookie") public SelenideElement accept;
    @Required @FindBy(css = "a#cn-close-notice") public SelenideElement close;

    @SuppressWarnings("unused")
    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        cookiesText.shouldHave(text("Utilizamos cookies para asegurar que damos la mejor experiencia al usuario en nuestra web. Si sigues utilizando este sitio asumiremos que estás de acuerdo."));
    }

    @SuppressWarnings("unused")
    @CheckReturnValue
    public void acceptCookies() {
        shouldLoadRequired();
        accept.click();
        getSelf().should(disappear);
    }

    @SuppressWarnings("unused")
    @CheckReturnValue
    public void acceptCookiesIfDisplayed() {
        if (getSelf().isDisplayed()) {
            acceptCookies();
        }
    }
}
