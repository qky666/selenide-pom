package integration.mtp.javapom;

import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.RequiredContainer;

public class MainFramePage implements RequiredContainer {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget();
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget();
}
