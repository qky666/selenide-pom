package com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom;

import com.github.qky666.selenidepom.Page;
import com.github.qky666.selenidepom.Required;

public class MainFramePage extends Page {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget();
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
