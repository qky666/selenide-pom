package integration.mtp.lazypom;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;
import lombok.Getter;

public class MainFramePage implements RequiredContainer {
    @Getter(lazy = true, onMethod_ = {@Required})
    private final MainMenuWidget mainMenu = new MainMenuWidget();
    @Getter(lazy = true)
    private final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
