package com.github.qky666.selenidepom.test.java.mtp.lazypom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.Widget;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class MainMenuWidget extends Widget {
    public MainMenuWidget() {
        this($("div.custom-menu"));
    }

    public MainMenuWidget(SelenideElement self) {
        super(self);
    }

    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement services = self.$("li#servicios_menu");
    @Getter(lazy = true)
    private final SelenideElement servicesPopUp = self.$("div.dropdown-servicios");
    @Getter(lazy = true)
    private final SelenideElement servicesPopUpQualityAssurance = self.$("div.dropdown-servicios a[data-principal='Aseguramiento de la calidad']");
}
