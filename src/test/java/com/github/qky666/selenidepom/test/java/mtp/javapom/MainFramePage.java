package com.github.qky666.selenidepom.test.java.mtp.javapom;

import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.RequiredContainer;

public class MainFramePage implements RequiredContainer {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget();
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
