package integration.mtp.pom.widgetpom;

import es.qky.selenidepom.Page;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CommonFramePage extends Page {
    @Required
    @FindBy(css = "div.custom-menu")
    public CommonFrameMainMenuWidget mainMenu;
}
