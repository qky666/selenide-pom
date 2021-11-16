package com.github.qky666.selenidepom.test.java.mtp.simplepom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.RequiredContainer;
import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.RequiredError;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class ServicesPage implements RequiredContainer {
    @Required
    public SelenideElement principal = $("div.servicios-principal");
    @Required
    public final SelenideElement title = $("h1.h2");
    public final SelenideElement badSelector = $("bad-selector");

    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        RequiredContainer.super.shouldLoadRequired(timeout);
        title.shouldHave(Condition.text("Aseguramiento de la calidad"), timeout);
    }
}
