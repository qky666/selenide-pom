package com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.Widget;

public class MainMenuWidget extends Widget {
    @Required
    public SelenideElement services() {
        return this.$("li#servicios_menu");
    }

    public SelenideElement servicesPopUp() {
        return this.$("div.dropdown-servicios");
    }

    public SelenideElement servicesPopUpQualityAssurance() {
        return servicesPopUp().$("a[data-principal='Aseguramiento de la calidad']");
    }

    public MainMenuWidget(SelenideElement self) {
        super(self);
    }
}
