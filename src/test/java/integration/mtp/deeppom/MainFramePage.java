package integration.mtp.deeppom;

import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.RequiredContainer;
import org.openqa.selenium.support.FindBy;

public class MainFramePage implements RequiredContainer {
    @Required
    @FindBy(css = "div.custom-menu")
    public MainMenuWidget mainMenu;
}
