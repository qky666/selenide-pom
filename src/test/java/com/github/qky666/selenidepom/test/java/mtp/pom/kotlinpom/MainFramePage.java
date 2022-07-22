package com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.Page;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage extends Page {

    // Fields
    @Getter(lazy = true) @Required("desktop") private final MainMenuWidget mainMenu = new MainMenuWidget();
    @Getter(lazy = true) @Required("mobile") private final SelenideElement mobileMenuButton = $("button.custom-menu-btn-flotante");
    @Getter(lazy = true) private final MobileMenuWidget mobileMenu = new MobileMenuWidget();
    @Getter(lazy = true) private final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
