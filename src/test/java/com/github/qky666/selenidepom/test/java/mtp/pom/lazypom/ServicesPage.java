package com.github.qky666.selenidepom.test.java.mtp.pom.lazypom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ServicesPage extends MainFramePage {

    // Fields
    @Getter(lazy = true, onMethod_ = {@Required}) private final SelenideElement principal = $("div.servicios-principal");
    @Getter(lazy = true, onMethod_ = {@Required}) private final SelenideElement title = $("h1.h2");
    @Getter(lazy = true) private final SelenideElement badSelector = $("bad-selector");

    //Constructors
    @Override
    public void customShouldLoadRequired(Duration timeout, String pomVersion) throws Throwable {
        super.customShouldLoadRequired(timeout, pomVersion);
        getTitle().shouldHave(text("Aseguramiento de la calidad"), timeout);
    }
}
