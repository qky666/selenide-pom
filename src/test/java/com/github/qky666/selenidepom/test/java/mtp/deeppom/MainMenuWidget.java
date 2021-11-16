package com.github.qky666.selenidepom.test.java.mtp.deeppom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.ElementsContainerWidget;
import com.github.qky666.selenidepom.java.Required;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("unused")
public class MainMenuWidget extends ElementsContainerWidget {
    @Required
    @FindBy(css = "li#servicios_menu")
    public SelenideElement services;
    @FindBy(css = "div.dropdown-servicios")
    public MainMenuServicesDropdownWidget servicesPopUp;
}
