package com.github.qky666.selenidepom.test.java.mtp.lazypom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.RequiredError;
import com.github.qky666.selenidepom.java.Widget;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class CookiesBannerWidget extends Widget {

    // Fields
    @Getter(lazy = true, onMethod_ = {@Required}) private final SelenideElement cookiesText = self.$("span#cn-notice-text");
    @Getter(lazy = true, onMethod_ = {@Required}) private final SelenideElement accept = self.$("a#cn-accept-cookie");
    @Getter(lazy = true, onMethod_ = {@Required}) private final SelenideElement close = self.$("a#cn-close-notice");

    // Constructors
    public CookiesBannerWidget() {this($("div.cookie-notice-container"));}

    public CookiesBannerWidget(SelenideElement self) {super(self);}

    // Methods
    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        getCookiesText().shouldHave(text("Utilizamos cookies para asegurar que damos la mejor experiencia al usuario en nuestra web. Si sigues utilizando este sitio asumiremos que estás de acuerdo."));
    }

    public void acceptCookies() {
        shouldLoadRequired();
        getAccept().click();
        self.should(disappear);
    }

    public void acceptCookiesIfDisplayed() {
        if (self.isDisplayed()) {
            acceptCookies();
        }
    }
}
