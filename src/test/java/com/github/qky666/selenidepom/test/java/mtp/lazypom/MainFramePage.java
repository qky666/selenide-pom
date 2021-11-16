package com.github.qky666.selenidepom.test.java.mtp.lazypom;

import com.github.qky666.selenidepom.java.RequiredContainer;
import com.github.qky666.selenidepom.java.Required;
import lombok.Getter;

public class MainFramePage implements RequiredContainer {
    @Getter(lazy = true, onMethod_ = {@Required})
    private final MainMenuWidget mainMenu = new MainMenuWidget();
    @Getter(lazy = true)
    private final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
