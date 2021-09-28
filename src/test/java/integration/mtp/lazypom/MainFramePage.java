package integration.mtp.lazypom;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage implements RequiredContainer {
    @Getter(lazy = true, onMethod_ = {@Required})
    private final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
    @Getter(lazy = true)
    private final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget($("div.cookie-notice-container"));
}
