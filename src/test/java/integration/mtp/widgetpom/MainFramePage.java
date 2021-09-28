package integration.mtp.widgetpom;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;
import org.openqa.selenium.support.FindBy;

public class MainFramePage implements RequiredContainer {
    @Required
    @FindBy(css = "div.custom-menu")
    public MainMenuWidget mainMenu;
    @FindBy(css = "div.cookie-notice-container")
    public CookiesBannerWidget cookiesBanner;
}
