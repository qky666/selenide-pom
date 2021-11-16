package com.github.qky666.selenidepom.test.java.mtp.constructorpom;

import com.github.qky666.selenidepom.java.RequiredContainer;
import com.github.qky666.selenidepom.java.Required;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage implements RequiredContainer {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget($("div.cookie-notice-container"));
}
