package com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom;

import com.github.qky666.selenidepom.pom.Page;
import com.github.qky666.selenidepom.annotation.Required;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage extends Page {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget($("div#cookie-law-info-bar"));
}
