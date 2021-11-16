package integration.mtp.javapom;

import com.github.qky666.selenidepom.kotlin.Page;
import com.github.qky666.selenidepom.kotlin.Required;

public class MainFramePage extends Page {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget();
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
