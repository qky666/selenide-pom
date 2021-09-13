package integration.mtp.pom.widgetpom;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class CommonFramePage implements RequiredContainer {
    @Required @FindBy(css = "div.custom-menu") public CommonFrameMainMenuWidget mainMenu;
}
