package integration.mtp.lazypom;

import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.RequiredContainer;
import lombok.Getter;

public class MainFramePage implements RequiredContainer {
    @Getter(lazy = true, onMethod_ = {@Required})
    private final MainMenuWidget mainMenu = new MainMenuWidget();
    @Getter(lazy = true)
    private final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
