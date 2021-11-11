package integration.mtp.constructorpom;

import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.RequiredContainer;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage implements RequiredContainer {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
    public final CookiesBannerWidget cookiesBanner = new CookiesBannerWidget($("div.cookie-notice-container"));
}
