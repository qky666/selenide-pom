package integration.mtp.widgetpom;

import com.github.qky666.selenidepom.kotlin.Page;
import com.github.qky666.selenidepom.kotlin.Required;
import org.openqa.selenium.support.FindBy;

public class MainFramePage extends Page {
    @Required
    @FindBy(css = "div.custom-menu")
    public MainMenuWidget mainMenu;
    @FindBy(css = "div.cookie-notice-container")
    public CookiesBannerWidget cookiesBanner;
}
