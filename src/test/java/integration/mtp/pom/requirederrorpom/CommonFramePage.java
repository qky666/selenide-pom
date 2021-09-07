package integration.mtp.pom.requirederrorpom;

import es.qky.selenidepom.Page;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CommonFramePage extends Page {
    @FindBy(css = "div.custom-menu")
    @Required
    public CommonFrameMainMenuWidget mainMenu;
}
