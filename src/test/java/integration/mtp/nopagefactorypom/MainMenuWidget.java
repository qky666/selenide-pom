package integration.mtp.nopagefactorypom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.kotlin.Required;
import com.github.qky666.selenidepom.kotlin.Widget;

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
