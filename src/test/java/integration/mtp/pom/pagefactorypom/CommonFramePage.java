package integration.mtp.pom.pagefactorypom;

import javax.annotation.ParametersAreNonnullByDefault;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;
import org.openqa.selenium.support.FindBy;


@ParametersAreNonnullByDefault
public class CommonFramePage implements RequiredContainer {
    @Required
    @FindBy(css = "div.custom-menu")
    public CommonFrameMainMenuWidget mainMenu;
}
