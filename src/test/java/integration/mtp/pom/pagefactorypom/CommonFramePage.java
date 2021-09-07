package integration.mtp.pom.pagefactorypom;

import javax.annotation.ParametersAreNonnullByDefault;

import es.qky.selenidepom.Page;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;


@ParametersAreNonnullByDefault
public class CommonFramePage extends Page {
    @Required
    @FindBy(css = "div.custom-menu")
    public CommonFrameMainMenuWidget mainMenu;
}
