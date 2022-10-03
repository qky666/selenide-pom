package com.github.qky666.selenidepom.test.java.mtp.pom.lazypom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.LoadableKt;
import com.github.qky666.selenidepom.pom.Widget;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;

public class CookiesBannerWidget extends Widget {

    // Fields
    @Getter(lazy = true, onMethod_ = {@Required}) private final SelenideElement cookiesText = $("div.cli-bar-message");
    @Getter(lazy = true, onMethod_ = {@Required}) private final SelenideElement accept = $("a#cookie_action_close_header");

    // Constructor
    public CookiesBannerWidget(SelenideElement self) {super(self);}

    // Methods
    @Override
    public void customShouldLoadRequired(Duration timeout, String pomVersion) throws Throwable {
        super.customShouldLoadRequired(timeout, pomVersion);
        getCookiesText().shouldHave(Condition.text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)"), timeout);
    }

    public void acceptCookies() {
        LoadableKt.shouldLoadRequired(this).getAccept().click();
        should(disappear);
    }
}
