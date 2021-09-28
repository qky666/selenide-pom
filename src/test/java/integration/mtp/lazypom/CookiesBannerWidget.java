package integration.mtp.lazypom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredError;
import es.qky.selenidepom.Widget;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;

public class CookiesBannerWidget extends Widget {
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement cookiesText = getSelf().$("span#cn-notice-text");
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement accept = getSelf().$("a#cn-accept-cookie");
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement close = getSelf().$("a#cn-close-notice");

    public CookiesBannerWidget(SelenideElement self) {
        super(self);
    }

    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        getCookiesText().shouldHave(text("Utilizamos cookies para asegurar que damos la mejor experiencia al usuario en nuestra web. Si sigues utilizando este sitio asumiremos que estás de acuerdo."));
    }

    public void acceptCookies() {
        shouldLoadRequired();
        getAccept().click();
        getSelf().should(disappear);
    }

    public void acceptCookiesIfDisplayed() {
        if (getSelf().isDisplayed()) {
            acceptCookies();
        }
    }
}
