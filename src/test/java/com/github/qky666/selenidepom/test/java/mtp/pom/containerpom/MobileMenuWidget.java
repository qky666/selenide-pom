package com.github.qky666.selenidepom.test.java.mtp.pom.containerpom;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.CollectionCondition.allMatch;

public class MobileMenuWidget extends ElementsContainer {

    // Fields
    // First level menu items. I only write one, but there are more
    @FindBy(xpath = ".//li[@aria-expanded]/a[.='Servicios']")
    @Required
    public SelenideElement services;

    // All first level menu items
    @FindBy(css = "li.uk-parent") public ElementsCollection firstLevelMenuItems;

    // Second level menú items. I only write one, but there are more
    @FindBy(xpath = ".//a[.='Aseguramiento de la calidad']") public SelenideElement servicesQualityAssurance;

    public void shouldBeCollapsed() {
        firstLevelMenuItems.shouldHave(allMatch("All firstLevelMenuItems have aria-expanded=false", (WebElement element) -> "false".equalsIgnoreCase(element.getAttribute("aria-expanded"))));
    }
}
