package com.github.qky666.selenidepom.test.java.mtp.pom.lazypom;

import com.github.qky666.selenidepom.pom.Page;
import com.github.qky666.selenidepom.annotation.Required;
import lombok.Getter;

public class MainFramePage extends Page {

    // Fields
    @Getter(lazy = true, onMethod_ = {@Required}) private final MainMenuWidget mainMenu = new MainMenuWidget();
    @Getter(lazy = true) private final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
