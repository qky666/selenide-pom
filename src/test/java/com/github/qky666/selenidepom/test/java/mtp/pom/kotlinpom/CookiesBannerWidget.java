package com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.error.RequiredError;
import com.github.qky666.selenidepom.pom.Widget;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class CookiesBannerWidget extends Widget {

    // Fields
    @Getter(lazy = true) @Required private final SelenideElement cookiesText = getSelf().$("div.cli-bar-message");
    @Getter(lazy = true) @Required private final SelenideElement accept = getSelf().$("a#cookie_action_close_header");

    // Constructors
    public CookiesBannerWidget() {this($("div#cookie-law-info-bar"));}

    public CookiesBannerWidget(SelenideElement self) {super(self);}

    // Methods
    @Override
    public void shouldLoadRequired(Duration timeout, String pomVersion) throws RequiredError {
        super.shouldLoadRequired(timeout, pomVersion);
        getCookiesText().shouldHave(text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)"));
    }

    public void acceptCookies() {
        shouldLoadRequired();
        getAccept().click();
        getSelf().should(disappear);
    }
}
