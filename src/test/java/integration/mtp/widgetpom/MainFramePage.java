package integration.mtp.widgetpom;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class MainFramePage implements RequiredContainer {
    @Required @FindBy(css = "div.custom-menu") public MainMenuWidget mainMenu;
    @FindBy(css = "div.cookie-notice-container") public CookiesBanner cookiesBanner;
}
