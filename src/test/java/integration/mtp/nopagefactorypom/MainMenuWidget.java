package integration.mtp.nopagefactorypom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.Widget;

import javax.annotation.ParametersAreNonnullByDefault;


@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class MainMenuWidget extends Widget {
    public MainMenuWidget(SelenideElement self) {
        super(self);
    }

    @Required public SelenideElement getServicesLnk() {return getSelf().$("li#servicios_menu");}
    public SelenideElement getServicesPopUp() {return getSelf().$("div.dropdown-servicios");}
    public SelenideElement getServicesPopUpQualityAssuranceLnk() {return getServicesPopUp().$("a[data-principal='Aseguramiento de la calidad']");}
}
