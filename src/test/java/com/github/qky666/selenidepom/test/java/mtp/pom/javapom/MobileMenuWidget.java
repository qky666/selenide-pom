package com.github.qky666.selenidepom.test.java.mtp.pom.javapom;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import com.github.qky666.selenidepom.pom.Widget;
import lombok.Getter;
import org.openqa.selenium.WebElement;

public class MobileMenuWidget extends Widget {

    // Fields
    // First level menu items. I only write one, but there are more
    @Getter(lazy = true) @Required private final SelenideElement services = $x(".//li[@aria-expanded]/a[.='Servicios']");

    // All first level menu items
    @Getter(lazy = true) private final ElementsCollection firstLevelMenuItems = $$("li.uk-parent");

    // Second level menú items. I only write one, but there are more
    @Getter(lazy = true) private final SelenideElement servicesQualityAssurance = $x(".//a[.='Aseguramiento de la calidad']");

    //Constructor
    public MobileMenuWidget(SelenideElement self) {super(self);}

    public void shouldBeCollapsed() {
        getFirstLevelMenuItems().shouldHave(CollectionCondition.allMatch(
                "All firstLevelMenuItems have aria-expanded=false",
                (WebElement element) -> "false".equalsIgnoreCase(element.getAttribute("aria-expanded"))
        ));
    }
}