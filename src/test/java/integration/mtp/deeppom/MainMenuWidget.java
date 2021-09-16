package integration.mtp.deeppom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.RequiredContainer;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;


@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class MainMenuWidget extends ElementsContainer implements RequiredContainer {
    @Required @FindBy(css = "li#servicios_menu") public SelenideElement servicesLnk;
    @FindBy(css = "div.dropdown-servicios") public MainMenuServicesDropdownWidget servicesPopUp;
}
