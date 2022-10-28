package com.github.qky666.selenidepom.test.java.mtp.pom.containerpom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Loadable;
import com.github.qky666.selenidepom.pom.Required;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;

import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;

public class CookiesBannerWidget extends ElementsContainer implements Loadable {

    // Fields
    @FindBy(css = "div.cli-bar-message") @Required public SelenideElement cookiesText;
    @FindBy(css = "a#cookie_action_close_header") @Required public SelenideElement accept;

    // Methods
    @Override
    public void customShouldLoadRequired(Duration timeout, String pomVersion, String lang) throws Throwable {
        Loadable.super.customShouldLoadRequired(timeout, pomVersion, lang);
        cookiesText.shouldHave(text("Utilizamos cookies propias y de terceros para fines analíticos y para mostrarte publicidad personalizada en base a un perfil elaborado a partir de tus hábitos de navegación (por ejemplo, páginas visitadas)"), timeout);
    }

    public void acceptCookies() {
        shouldLoadRequired(this).accept.click();
        getSelf().should(disappear);
    }
}
