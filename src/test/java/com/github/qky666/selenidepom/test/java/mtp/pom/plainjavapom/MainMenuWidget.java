package com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.Widget;

import static com.codeborne.selenide.Selenide.$;

public class MainMenuWidget extends Widget {
    @Required public final SelenideElement services;
    @SuppressWarnings("unused") public final SelenideElement servicesPopUp;
    public final SelenideElement servicesPopUpQualityAssurance;

    public MainMenuWidget() {
        this($("div.custom-menu"));
    }

    public MainMenuWidget(SelenideElement self) {
        super(self);
        services = self.$("li#servicios_menu");
        servicesPopUp = self.$("div.dropdown-servicios");
        servicesPopUpQualityAssurance = self.$("div.dropdown-servicios a[data-principal='Aseguramiento de la calidad']");
    }
}
