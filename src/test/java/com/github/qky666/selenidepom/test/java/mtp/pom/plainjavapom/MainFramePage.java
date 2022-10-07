package com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom;

import com.codeborne.selenide.ClickOptions;
import com.github.qky666.selenidepom.pom.Page;
import com.github.qky666.selenidepom.pom.Required;

import static com.codeborne.selenide.Selenide.$;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;

public class MainFramePage extends Page {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget($("div#cookie-law-info-bar"));

    public void acceptCookies() {
        // This is what it should be, but for some reason, cookies message is not displayed when page loads
        // shouldLoadRequired(this).cookiesBanner.acceptCookies();

        // Workaround
        acceptCookiesDesktop();
    }

    private void acceptCookiesDesktop() {
        mainMenu.openSearch.click();
        mainMenu.langEs.click(ClickOptions.withOffset(0, -50));
        cookiesBanner.acceptCookies();
        shouldLoadRequired(this);
    }
}
