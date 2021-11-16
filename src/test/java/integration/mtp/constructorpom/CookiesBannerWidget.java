package integration.mtp.constructorpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.kotlin.Required;
import com.github.qky666.selenidepom.kotlin.RequiredError;
import com.github.qky666.selenidepom.kotlin.Widget;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;

public class CookiesBannerWidget extends Widget {
    @Required public final SelenideElement cookiesText;
    @Required public final SelenideElement accept;
    @Required public final SelenideElement close;

    public CookiesBannerWidget(SelenideElement self) {
        super(self);
        cookiesText = getSelf().$("span#cn-notice-text");
        accept = getSelf().$("a#cn-accept-cookie");
        close = getSelf().$("a#cn-close-notice");
    }

    @Override
    public void shouldLoadRequiredWithTimeout(Duration timeout) throws RequiredError {
        super.shouldLoadRequiredWithTimeout(timeout);
        cookiesText.shouldHave(text("Utilizamos cookies para asegurar que damos la mejor experiencia al usuario en nuestra web. Si sigues utilizando este sitio asumiremos que estás de acuerdo."));
    }

    public void acceptCookies() {
        shouldLoadRequired();
        accept.click();
        getSelf().should(disappear);
    }

    public void acceptCookiesIfDisplayed() {
        if (getSelf().isDisplayed()) {
            acceptCookies();
        }
    }
}
