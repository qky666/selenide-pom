package integration.mtp.pom.pagefactory.commonframe;

import javax.annotation.ParametersAreNonnullByDefault;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Page;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;


@ParametersAreNonnullByDefault
public class MtpCommonFramePage extends Page {
    @FindBy(css = "div.custom-menu") @Required public MainMenuContainer mainMenu;
    @FindBy(css = "bad-selector") public SelenideElement badSelector;
}
