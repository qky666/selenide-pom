package com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.Widget;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class MobileMenuWidget extends Widget {

    // Fields
    @Getter(lazy = true) @Required private final SelenideElement services = getSelf().$x(".//li[@aria-expanded]/a[.='Servicios']");
    @Getter(lazy = true) private final SelenideElement servicesQualityAssurance = getSelf().$x(".//a[.='Aseguramiento de la calidad']");

    //Constructors
    public MobileMenuWidget() {this($("div#menu-movil ul.uk-nav"));}

    public MobileMenuWidget(SelenideElement self) {super(self);}

}
