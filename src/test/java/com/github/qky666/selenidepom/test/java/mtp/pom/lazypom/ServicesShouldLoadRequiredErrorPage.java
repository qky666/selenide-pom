package com.github.qky666.selenidepom.test.java.mtp.pom.lazypom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.error.RequiredError;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {

    // Fields
    @Getter(lazy = true, onMethod_ = {@Required}) private final SelenideElement principal = $("div.servicios-principal");
    @Getter(lazy = true, onMethod_ = {@Required}) private final SelenideElement title = $("h1.h2");

    // Methods
    @Override
    public void shouldLoadRequired(Duration timeout, String pomVersion) throws RequiredError {
        super.shouldLoadRequired(timeout, pomVersion);
        getTitle().shouldHave(text("Aseguramiento de la calidad con error"), timeout);
    }
}
