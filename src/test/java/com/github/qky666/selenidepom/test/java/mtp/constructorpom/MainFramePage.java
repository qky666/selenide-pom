package com.github.qky666.selenidepom.test.java.mtp.constructorpom;

import com.github.qky666.selenidepom.Page;
import com.github.qky666.selenidepom.Required;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage extends Page {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget($("div#cookie-law-info-bar"));
}
