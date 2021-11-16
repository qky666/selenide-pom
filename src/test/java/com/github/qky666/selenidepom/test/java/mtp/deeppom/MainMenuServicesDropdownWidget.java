package com.github.qky666.selenidepom.test.java.mtp.deeppom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.ElementsContainerWidget;
import com.github.qky666.selenidepom.java.Required;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("unused")
public class MainMenuServicesDropdownWidget extends ElementsContainerWidget {
    @Required
    @FindBy(css = "a[data-principal='Aseguramiento de la calidad']")
    public SelenideElement qualityAssurance;
}
