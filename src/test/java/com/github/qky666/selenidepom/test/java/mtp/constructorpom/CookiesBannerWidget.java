package com.github.qky666.selenidepom.test.java.mtp.constructorpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.RequiredError;
import com.github.qky666.selenidepom.java.Widget;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;

public class CookiesBannerWidget extends Widget {
    @Required public final SelenideElement cookiesText;
    @Required public final SelenideElement accept;
    @Required public final SelenideElement close;

    public CookiesBannerWidget(SelenideElement self) {
        super(self);
        cookiesText = self.$("span#cn-notice-text");
        accept = self.$("a#cn-accept-cookie");
        close = self.$("a#cn-close-notice");
    }

    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        cookiesText.shouldHave(text("Utilizamos cookies para asegurar que damos la mejor experiencia al usuario en nuestra web. Si sigues utilizando este sitio asumiremos que estás de acuerdo."));
    }

    public void acceptCookies() {
        shouldLoadRequired();
        accept.click();
        self.should(disappear);
    }

    public void acceptCookiesIfDisplayed() {
        if (self.isDisplayed()) {
            acceptCookies();
        }
    }
}
