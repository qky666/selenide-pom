package com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.Widget;
import lombok.Getter;

public class MainMenuWidget extends Widget {

    // Fields
    @Getter(lazy = true) @Required private final SelenideElement services = this.$("li#servicios_menu");
    @Getter(lazy = true) private final SelenideElement servicesPopUp = this.$("div.dropdown-servicios");
    @Getter(lazy = true) private final SelenideElement servicesPopUpQualityAssurance = getServicesPopUp().$("a[data-principal='Aseguramiento de la calidad']");

    //Constructor
    public MainMenuWidget(SelenideElement self) {super(self);}

}
