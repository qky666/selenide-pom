package com.github.qky666.selenidepom.test.java.mtp.pom.constructorpom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import com.github.qky666.selenidepom.pom.Widget;

@SuppressWarnings("unused")
public class MainMenuWidget extends Widget {
    @Required public final SelenideElement services;
    @Required public final SelenideElement sectors;
    @Required public final SelenideElement training;
    @Required public final SelenideElement blog;
    @Required public final SelenideElement talent;
    @Required public final SelenideElement about;
    @Required public final SelenideElement contact;
    @Required public final SelenideElement langEn;
    @Required public final SelenideElement langEs;
    public final SelenideElement openSearch;

    public final SelenideElement servicesPopUp;
    public final SelenideElement servicesPopUpQualityAssurance;

    public MainMenuWidget(SelenideElement self) {
        super(self);

        services = $("li#servicios_menu>a");
        sectors = $("li#sectores_menu>a");
        training = $("li#formacion_menu>a");
        blog = $("li#blog_menu>a");
        talent = $("li#talento_menu>a");
        about = $("li.sobre_menu>a");
        contact = $("li#contacto_menu>a");
        langEn = $$("li.individual-menu-idioma>a").findBy(Condition.text("en"));
        langEs = $$("li.individual-menu-idioma>a").findBy(Condition.text("es"));
        openSearch = $("button#btn-menu");

        servicesPopUp = $("div.dropdown-servicios");
        servicesPopUpQualityAssurance = $("div.dropdown-servicios a[data-principal='Aseguramiento de la calidad']");
    }
}
