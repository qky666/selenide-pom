package com.github.qky666.selenidepom.test.java.mtp.pom.javapom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import com.github.qky666.selenidepom.pom.Widget;
import lombok.Getter;

public class MainMenuWidget extends Widget {

    // Fields
    @Getter(lazy = true) @Required private final SelenideElement home = $("a.img-menu");
    @Getter(lazy = true) @Required private final SelenideElement services = $("li#servicios_menu>a");
    @Getter(lazy = true) @Required private final SelenideElement sectors = $("li#sectores_menu>a");
    @Getter(lazy = true) @Required private final SelenideElement training = $("li#formacion_menu>a");
    @Getter(lazy = true) @Required private final SelenideElement blog = $("li#blog_menu>a");
    @Getter(lazy = true) @Required private final SelenideElement talent = $("li#talento_menu>a");
    @Getter(lazy = true) @Required private final SelenideElement about = $("li.sobre_menu>a");
    @Getter(lazy = true) @Required private final SelenideElement contact = $("li#contacto_menu>a");
    @Getter(lazy = true) @Required private final SelenideElement langEn = $$("li.individual-menu-idioma>a").findBy(Condition.text("en"));
    @Getter(lazy = true) @Required private final SelenideElement langEs = $$("li.individual-menu-idioma>a").findBy(Condition.text("es"));
    @Getter(lazy = true) @Required private final SelenideElement openSearch = $("button#btn-menu");

    @Getter(lazy = true) private final SelenideElement servicesPopUp = $("div.dropdown-servicios");
    @Getter(lazy = true) private final SelenideElement servicesPopUpQualityAssurance = getServicesPopUp().$("a[data-principal='Aseguramiento de la calidad']");

    //Constructor
    public MainMenuWidget(SelenideElement self) {super(self);}
}
