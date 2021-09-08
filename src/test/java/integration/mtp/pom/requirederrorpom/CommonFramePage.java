package integration.mtp.pom.requirederrorpom;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredLoadable;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CommonFramePage implements RequiredLoadable {
    @FindBy(css = "div.custom-menu")
    @Required
    public CommonFrameMainMenuWidget mainMenu;
}
