package integration.mtp.lazypom;

import com.github.qky666.selenidepom.kotlin.Page;
import com.github.qky666.selenidepom.kotlin.Required;
import lombok.Getter;

public class MainFramePage extends Page {
    @Getter(lazy = true, onMethod_ = {@Required})
    private final MainMenuWidget mainMenu = new MainMenuWidget();
    @Getter(lazy = true)
    private final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
