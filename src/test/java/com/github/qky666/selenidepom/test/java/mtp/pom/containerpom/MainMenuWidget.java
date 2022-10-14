package com.github.qky666.selenidepom.test.java.mtp.pom.containerpom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import org.openqa.selenium.support.FindBy;

public class MainMenuWidget extends ElementsContainer {

    // Fields
    @FindBy(css = "li#servicios_menu>a") @Required public SelenideElement services;
    @FindBy(css = "li#sectores_menu>a") @Required public SelenideElement sectors;
    @FindBy(css = "li#formacion_menu>a") @Required public SelenideElement training;
    @FindBy(css = "li#blog_menu>a") @Required public SelenideElement blog;
    @FindBy(css = "li#talento_menu>a") @Required public SelenideElement talent;
    @FindBy(css = "li.sobre_menu>a") @Required public SelenideElement about;
    @FindBy(css = "li#contacto_menu>a") @Required public SelenideElement contact;
    @FindBy(xpath = ".//li[contains(@class,'individual-menu-idioma')]/a[contains(., 'en')]") @Required public SelenideElement langEn;
    @FindBy(xpath = ".//li[contains(@class,'individual-menu-idioma')]/a[contains(., 'es')]") @Required public SelenideElement langEs;
    @FindBy(css = "button#btn-menu") @Required public SelenideElement openSearch;

    @FindBy(css = "div.dropdown-servicios") public SelenideElement servicesPopUp;
    @FindBy(css = "div.dropdown-servicios a[data-principal='Aseguramiento de la calidad']") public SelenideElement servicesPopUpQualityAssurance;
}
