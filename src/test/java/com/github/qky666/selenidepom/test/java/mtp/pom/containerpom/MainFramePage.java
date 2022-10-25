package com.github.qky666.selenidepom.test.java.mtp.pom.containerpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.config.SPConfig;
import com.github.qky666.selenidepom.pom.Loadable;
import com.github.qky666.selenidepom.pom.Required;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.ClickOptions.withOffset;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;

public class MainFramePage implements Loadable {

    // Fields
    @FindBy(css = "a.img-menu") @Required public SelenideElement home;
    @FindBy(css = "nav.menu-pc") @Required(pomVersion = "desktop") public MainMenuWidget mainMenu;
    @FindBy(css = "button.custom-menu-btn-flotante") @Required(pomVersion = "mobile") public SelenideElement mobileMenuButton;
    @FindBy(css = "div#menu-movil ul.uk-nav") public MobileMenuWidget mobileMenu;
    @FindBy(css = "div#cookie-law-info-bar") public CookiesBannerWidget cookiesBanner;

    // Methods
    public void acceptCookies() {
        // This is what it should be, but for some reason, cookies message is not displayed when page loads
        // shouldLoadRequired(this).getCookiesBanner().acceptCookies();

        // Workaround
        if (SPConfig.INSTANCE.getPomVersion().equals("mobile")) {
            acceptCookiesMobile();
        } else {
            acceptCookiesDesktop();
        }
    }

    private void acceptCookiesDesktop() {
        mainMenu.openSearch.click();
        mainMenu.langEs.click(withOffset(0, -50));
        cookiesBanner.acceptCookies();
        shouldLoadRequired(this);
    }

    private void acceptCookiesMobile() {
        shouldLoadRequired(this).mobileMenuButton.click();
        cookiesBanner.acceptCookies();
    }
}
