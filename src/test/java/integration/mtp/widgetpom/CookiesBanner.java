package integration.mtp.widgetpom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
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
    @Required @FindBy(css = "span#cn-notice-text") public SelenideElement cookiesTextTxt;
    @Required @FindBy(css = "a#cn-accept-cookie") public SelenideElement acceptLnk;
    @Required @FindBy(css = "a#cn-close-notice") public SelenideElement closeLnk;

    @SuppressWarnings("unused")
    @Override
    public void shouldLoadRequired(Duration timeout) throws Throwable {
        super.shouldLoadRequired(timeout);
        cookiesTextTxt.shouldHave(text("Utilizamos cookies para asegurar que damos la mejor experiencia al usuario en nuestra web. Si sigues utilizando este sitio asumiremos que estás de acuerdo."));
    }

    @SuppressWarnings("unused")
    @CheckReturnValue
    public void acceptCookies() throws Throwable {
        shouldLoadRequired();
        acceptLnk.click();
        getSelf().should(disappear);
    }
}
