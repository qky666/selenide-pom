package com.github.qky666.selenidepom.test.java.mtp.pom.javapom;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.config.SPConfig;
import com.github.qky666.selenidepom.pom.Page;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;

public class MainFramePage extends Page {

    // Fields
    @Getter(lazy = true) @Required("desktop") private final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
    @Getter(lazy = true) @Required("mobile") private final SelenideElement mobileMenuButton = $("button.custom-menu-btn-flotante");
    @Getter(lazy = true) private final MobileMenuWidget mobileMenu = new MobileMenuWidget($("div#menu-movil ul.uk-nav"));
    @Getter(lazy = true) private final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget($("div#cookie-law-info-bar"));

    // Methods
    public void acceptCookies() {
        // This is what it should be, but for some reason, cookies message is not displayed when page loads
        // shouldLoadRequired(this).getCookiesBanner().acceptCookies();

        // Workaround
        if (SPConfig.INSTANCE.getPomVersion().equals("mobile")) {
            acceptCookiesMobile();
        } else {
            acceptCookiesDesktop();
        }
    }

    private void acceptCookiesDesktop() {
        getMainMenu().getOpenSearch().click();
        getMainMenu().getLangEs().click(ClickOptions.withOffset(0, -50));
        getCookiesBanner().acceptCookies();
        shouldLoadRequired(this);
    }

    private void acceptCookiesMobile() {
        shouldLoadRequired(this).getMobileMenuButton().click();
        getCookiesBanner().acceptCookies();
    }
}
