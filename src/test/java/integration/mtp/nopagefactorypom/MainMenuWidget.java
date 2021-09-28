package integration.mtp.nopagefactorypom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.Widget;

public class MainMenuWidget extends Widget {
    @Required
    public SelenideElement services() {
        return getSelf().$("li#servicios_menu");
    }

    public SelenideElement servicesPopUp() {
        return getSelf().$("div.dropdown-servicios");
    }

    public SelenideElement servicesPopUpQualityAssurance() {
        return servicesPopUp().$("a[data-principal='Aseguramiento de la calidad']");
    }

    public MainMenuWidget(SelenideElement self) {
        super(self);
    }
}
