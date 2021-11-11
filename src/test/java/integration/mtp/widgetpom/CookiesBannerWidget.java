package integration.mtp.widgetpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.RequiredError;
import com.github.qky666.selenidepom.Widget;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;

@SuppressWarnings("unused")
public class CookiesBannerWidget extends Widget {
    @Required
    @FindBy(css = "span#cn-notice-text")
    public SelenideElement cookiesText;
    @Required
    @FindBy(css = "a#cn-accept-cookie")
    public SelenideElement accept;
    @Required
    @FindBy(css = "a#cn-close-notice")
    public SelenideElement close;

    @SuppressWarnings("unused")
    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        cookiesText.shouldHave(text("Utilizamos cookies para asegurar que damos la mejor experiencia al usuario en nuestra web. Si sigues utilizando este sitio asumiremos que estás de acuerdo."));
    }

    @SuppressWarnings("unused")
    public void acceptCookies() {
        shouldLoadRequired();
        accept.click();
        getSelf().should(disappear);
    }

    @SuppressWarnings("unused")
    public void acceptCookiesIfDisplayed() {
        if (getSelf().isDisplayed()) {
            acceptCookies();
        }
    }
}
