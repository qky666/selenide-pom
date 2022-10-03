package com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.Widget;

public class MainMenuWidget extends Widget {
    // Elements
    @Required
    public SelenideElement home() {return $("a.img-menu");}

    @Required
    public SelenideElement services() {return $("li#servicios_menu>a");}

    @Required
    public SelenideElement sectors() {return $("li#sectores_menu>a");}

    @Required
    public SelenideElement training() {return $("li#formacion_menu>a");}

    @Required
    public SelenideElement blog() {return $("li#blog_menu>a");}

    @Required
    public SelenideElement talent() {return $("li#talento_menu>a");}

    @Required
    public SelenideElement about() {return $("li.sobre_menu>a");}

    @Required
    public SelenideElement contact() {return $("li#contacto_menu>a");}

    @Required
    public SelenideElement langEn() {return $$("li.individual-menu-idioma>a").findBy(Condition.text("en"));}

    @Required
    public SelenideElement langEs() {return $$("li.individual-menu-idioma>a").findBy(Condition.text("es"));}

    @Required
    public SelenideElement openSearch() {return $("button#btn-menu");}

    public SelenideElement servicesPopUp() {
        return $("div.dropdown-servicios");
    }

    public SelenideElement servicesPopUpQualityAssurance() {
        return servicesPopUp().$("a[data-principal='Aseguramiento de la calidad']");
    }

    // Constructor
    public MainMenuWidget(SelenideElement self) {
        super(self);
    }
}
