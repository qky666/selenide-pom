package com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.Widget;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class MainMenuWidget extends Widget {

    // Fields
    @Getter(lazy = true) @Required private final SelenideElement services = getSelf().$("li#servicios_menu");
    @Getter(lazy = true) private final SelenideElement servicesPopUp = getSelf().$("div.dropdown-servicios");
    @Getter(lazy = true) private final SelenideElement servicesPopUpQualityAssurance = getServicesPopUp().$("a[data-principal='Aseguramiento de la calidad']");

    //Constructors
    public MainMenuWidget() {this($("div.custom-menu"));}

    public MainMenuWidget(SelenideElement self) {super(self);}

}
