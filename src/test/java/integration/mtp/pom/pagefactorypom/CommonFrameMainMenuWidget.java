package integration.mtp.pom.pagefactorypom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.RequiredContainer;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CommonFrameMainMenuWidget extends ElementsContainer implements RequiredContainer {
    @Required
    @FindBy(css = "li#servicios_menu")
    public SelenideElement servicesLnk;

    @FindBy(css = "div.dropdown-servicios")
    public CommonFrameMainMenuServicesDropdownWidget servicesPopUp;
}
