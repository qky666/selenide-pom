package com.github.qky666.selenidepom.test.java.mtp.javapom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.RequiredError;
import com.github.qky666.selenidepom.Widget;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class CookiesBannerWidget extends Widget {
    @Required public final SelenideElement cookiesText;
    @Required public final SelenideElement accept;

    public CookiesBannerWidget() {
        this($("div#cookie-law-info-bar"));
    }

    public CookiesBannerWidget(SelenideElement self) {
        super(self);
        cookiesText = self.$("div.cli-bar-message");
        accept = self.$("a#cookie_action_close_header");
    }

    @Override
    public void shouldLoadRequired(Duration timeout, String pomVersion) throws RequiredError {
        super.shouldLoadRequired(timeout, pomVersion);
        cookiesText.shouldHave(text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)"));
    }

    public void acceptCookies() {
        shouldLoadRequired();
        accept.click();
        getSelf().should(disappear);
    }
}
