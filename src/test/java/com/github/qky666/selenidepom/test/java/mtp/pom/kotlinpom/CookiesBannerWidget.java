package com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.error.RequiredError;
import com.github.qky666.selenidepom.pom.Widget;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;

public class CookiesBannerWidget extends Widget<CookiesBannerWidget> {

    // Fields
    @Getter(lazy = true) @Required private final SelenideElement cookiesText = this.$("div.cli-bar-message");
    @Getter(lazy = true) @Required private final SelenideElement accept = this.$("a#cookie_action_close_header");

    // Constructor
    public CookiesBannerWidget(SelenideElement self) {super(self);}

    // Methods
    @Override
    public void shouldLoadRequired(Duration timeout, String pomVersion) throws RequiredError {
        super.shouldLoadRequired(timeout, pomVersion);
        getCookiesText().shouldHave(
                Condition.text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)")
        );
    }

    public void acceptCookies() {
        shouldLoadRequired();
        getAccept().click();
        this.should(disappear);
    }
}
