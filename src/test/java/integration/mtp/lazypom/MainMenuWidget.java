package integration.mtp.lazypom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.Widget;
import lombok.Getter;

public class MainMenuWidget extends Widget {
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement services = getSelf().$("li#servicios_menu");
    @Getter(lazy = true)
    private final SelenideElement servicesPopUp = getSelf().$("div.dropdown-servicios");
    @Getter(lazy = true)
    private final SelenideElement servicesPopUpQualityAssurance = getSelf().$("div.dropdown-servicios a[data-principal='Aseguramiento de la calidad']");

    public MainMenuWidget(SelenideElement self) {
        super(self);
    }
}
