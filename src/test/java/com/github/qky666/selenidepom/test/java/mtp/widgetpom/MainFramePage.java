package com.github.qky666.selenidepom.test.java.mtp.widgetpom;

import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.RequiredContainer;
import org.openqa.selenium.support.FindBy;

public class MainFramePage implements RequiredContainer {
    @Required
    @FindBy(css = "div.custom-menu")
    public MainMenuWidget mainMenu;
    @FindBy(css = "div.cookie-notice-container")
    public CookiesBannerWidget cookiesBanner;
}
