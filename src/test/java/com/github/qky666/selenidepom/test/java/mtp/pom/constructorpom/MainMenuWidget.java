package com.github.qky666.selenidepom.test.java.mtp.pom.constructorpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.Widget;

@SuppressWarnings("unused")
public class MainMenuWidget extends Widget {
    @Required public final SelenideElement services;
    public final SelenideElement servicesPopUp;
    public final SelenideElement servicesPopUpQualityAssurance;

    public MainMenuWidget(SelenideElement self) {
        super(self);
        services = this.$("li#servicios_menu");
        servicesPopUp = this.$("div.dropdown-servicios");
        servicesPopUpQualityAssurance = this.$("div.dropdown-servicios a[data-principal='Aseguramiento de la calidad']");
    }
}
