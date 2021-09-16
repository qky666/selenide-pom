package integration.mtp.deeppom;

import javax.annotation.ParametersAreNonnullByDefault;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;
import org.openqa.selenium.support.FindBy;


@ParametersAreNonnullByDefault
public class MainFramePage implements RequiredContainer {
    @Required @FindBy(css = "div.custom-menu") public MainMenuWidget mainMenu;
}
