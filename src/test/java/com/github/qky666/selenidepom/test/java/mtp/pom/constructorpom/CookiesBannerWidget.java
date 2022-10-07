package com.github.qky666.selenidepom.test.java.mtp.pom.constructorpom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import com.github.qky666.selenidepom.pom.Widget;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;

public class CookiesBannerWidget extends Widget {
    @Required public final SelenideElement cookiesText;
    @Required public final SelenideElement accept;

    public CookiesBannerWidget(SelenideElement self) {
        super(self);
        cookiesText = $("div.cli-bar-message");
        accept = $("a#cookie_action_close_header");
    }

    @Override
    public void customShouldLoadRequired(Duration timeout, String pomVersion) throws Throwable {
        super.customShouldLoadRequired(timeout, pomVersion);
        this.cookiesText.shouldHave(Condition.text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)"), timeout);
    }

    public void acceptCookies() {
        shouldLoadRequired(this).accept.click();
        this.should(disappear);
    }
}
