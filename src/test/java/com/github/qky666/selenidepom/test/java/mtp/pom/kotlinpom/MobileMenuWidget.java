package com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.Widget;
import lombok.Getter;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;

public class MobileMenuWidget extends Widget {

    // Fields
    // First level menu items. I only write one, but there are more
    @Getter(lazy = true)
    @Required
    private final SelenideElement services = getSelf().$x(".//li[@aria-expanded]/a[.='Servicios']");

    // All first level menu items
    @Getter(lazy = true) private final ElementsCollection firstLevelMenuItems = getSelf().$$("li.uk-parent");

    // Second level menú items. I only write one, but there are more
    @Getter(lazy = true) private final SelenideElement servicesQualityAssurance = getSelf().$x(".//a[.='Aseguramiento de la calidad']");

    //Constructors
    public MobileMenuWidget() {this($("div#menu-movil ul.uk-nav"));}

    public MobileMenuWidget(SelenideElement self) {super(self);}

    public void shouldBeCollapsed() {
        getFirstLevelMenuItems().shouldHave(CollectionCondition.allMatch(
                "All firstLevelMenuItems have aria-expanded=false",
                (WebElement element) -> "false".equalsIgnoreCase(element.getAttribute("aria-expanded"))
        ));
    }
}
