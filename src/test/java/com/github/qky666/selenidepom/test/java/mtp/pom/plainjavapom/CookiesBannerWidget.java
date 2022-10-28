package com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import com.github.qky666.selenidepom.pom.LoadableKt;
import com.github.qky666.selenidepom.pom.Widget;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;

public class CookiesBannerWidget extends Widget {
    @Required public final SelenideElement cookiesText;
    @Required public final SelenideElement accept;

    public CookiesBannerWidget(SelenideElement self) {
        super(self);
        cookiesText = $("div.cli-bar-message");
        accept = $("a#cookie_action_close_header");
    }

    @Override
    public void customShouldLoadRequired(Duration timeout, String pomVersion, String lang) throws Throwable {
        super.customShouldLoadRequired(timeout, pomVersion, lang);
        cookiesText.shouldHave(Condition.text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)"), timeout);
    }

    public void acceptCookies() {
        LoadableKt.shouldLoadRequired(this).accept.click();
        should(disappear);
    }
}
