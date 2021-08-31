package integration.mtp.pom.pagefactory.commonframe;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Container;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

public class MainMenuContainer extends Container {
    @FindBy(css = "li#servicios_menu") @Required public SelenideElement servicesLnk;
    @FindBy(css = "div.dropdown-servicios") public ServicesDropdownContainer servicesPopUp;
}
