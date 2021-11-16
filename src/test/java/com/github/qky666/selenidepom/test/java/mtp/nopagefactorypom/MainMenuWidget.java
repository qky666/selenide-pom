package com.github.qky666.selenidepom.test.java.mtp.nopagefactorypom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.Widget;

public class MainMenuWidget extends Widget {
    @Required
    public SelenideElement services() {
        return self.$("li#servicios_menu");
    }

    public SelenideElement servicesPopUp() {
        return self.$("div.dropdown-servicios");
    }

    public SelenideElement servicesPopUpQualityAssurance() {
        return servicesPopUp().$("a[data-principal='Aseguramiento de la calidad']");
    }

    public MainMenuWidget(SelenideElement self) {
        super(self);
    }
}
