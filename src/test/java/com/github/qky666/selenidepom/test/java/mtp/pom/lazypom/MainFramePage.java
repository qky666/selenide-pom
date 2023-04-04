package com.github.qky666.selenidepom.test.java.mtp.pom.lazypom;

import com.codeborne.selenide.ClickOptions;
import com.github.qky666.selenidepom.pom.Page;
import com.github.qky666.selenidepom.pom.Required;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;
import static com.github.qky666.selenidepom.pom.LoadableKt.hasLoadedRequired;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;

public class MainFramePage extends Page {

    // Fields
    @Getter(lazy = true, onMethod_ = {@Required}) private final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
    @Getter(lazy = true) private final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget($("div#cookie-law-info-bar"));

    // Methods
    public void acceptCookies() {
        // This is what it should be, but for some reason, cookies message is not displayed when page loads
        // shouldLoadRequired(this).getCookiesBanner().acceptCookies();

        // Workaround
        acceptCookiesDesktop();
    }

    private void acceptCookiesDesktop() {
        do {
            getMainMenu().getOpenSearch().click();
            getMainMenu().getLangEs().click(ClickOptions.withOffset(0, -50));
        } while (!hasLoadedRequired(getCookiesBanner()));
        getCookiesBanner().acceptCookies();
        shouldLoadRequired(this);
    }
}
