package com.github.qky666.selenidepom.test.java.mtp.simplepom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.Page;
import com.github.qky666.selenidepom.Required;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage extends Page {
    @Required public final SelenideElement mainMenu = $("div.custom-menu");
    @Required public final SelenideElement mainMenuServices = mainMenu.$("li#servicios_menu");
    public final SelenideElement mainMenuServicesPopUp = mainMenu.$("div.dropdown-servicios");
    public final SelenideElement mainMenuServicesPopUpQualityAssurance = mainMenuServicesPopUp.$("a[data-principal='Aseguramiento de la calidad']");
}
