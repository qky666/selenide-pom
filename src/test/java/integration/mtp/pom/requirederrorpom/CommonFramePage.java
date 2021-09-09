package integration.mtp.pom.requirederrorpom;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CommonFramePage implements RequiredContainer {
    @FindBy(css = "div.custom-menu")
    @Required
    public CommonFrameMainMenuWidget mainMenu;
}
