package com.github.qky666.selenidepom.test.java.mtp.pom.constructorpom;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import com.github.qky666.selenidepom.pom.Page;

import static com.codeborne.selenide.Selenide.$;
import static com.github.qky666.selenidepom.pom.LoadableKt.hasLoadedRequired;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;

public class MainFramePage extends Page {
    @Required public final SelenideElement home = $("a.img-menu");
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget($("nav.menu-pc"));
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget($("div#cookie-law-info-bar"));

    public void acceptCookies() {
        // This is what it should be, but for some reason, cookies message is not displayed when page loads
        // shouldLoadRequired(this).cookiesBanner.acceptCookies();

        // Workaround
        acceptCookiesDesktop();
    }

    private void acceptCookiesDesktop() {
        do {
            mainMenu.openSearch.click();
            mainMenu.langEs.click(ClickOptions.withOffset(0, -50));
        } while(!hasLoadedRequired(cookiesBanner));
        cookiesBanner.acceptCookies();
        shouldLoadRequired(this);
    }
}
