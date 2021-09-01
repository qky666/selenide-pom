package integration.mtp.pom.pagefactory;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Widget;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CommonFrameMainMenuWidget extends Widget {
    @Required
    @FindBy(css = "li#servicios_menu")
    public SelenideElement servicesLnk;

    @FindBy(css = "div.dropdown-servicios")
    public CommonFrameMainMenuServicesDropdownWidget servicesPopUp;
}
