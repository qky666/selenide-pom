package integration.mtp.constructorpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.Widget;

@SuppressWarnings("unused")
public class MainMenuWidget extends Widget {
    @Required public final SelenideElement services;
    public final SelenideElement servicesPopUp;
    public final SelenideElement servicesPopUpQualityAssurance;

    public MainMenuWidget(SelenideElement self) {
        super(self);
        services = getSelf().$("li#servicios_menu");
        servicesPopUp = getSelf().$("div.dropdown-servicios");
        servicesPopUpQualityAssurance = getSelf().$("div.dropdown-servicios a[data-principal='Aseguramiento de la calidad']");
    }
}
