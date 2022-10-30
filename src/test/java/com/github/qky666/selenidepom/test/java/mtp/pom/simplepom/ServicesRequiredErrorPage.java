package com.github.qky666.selenidepom.test.java.mtp.pom.simplepom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class ServicesRequiredErrorPage extends MainFramePage {
    @Required public SelenideElement principal = $("div.servicios-principal");
    @Required public final SelenideElement title = $("h1.h2");
    // badSelector generates an error in shouldLoadRequired
    @Required public final SelenideElement badSelector = $("bad-selector");


    @Override
    public void customShouldLoadRequired(Duration timeout, String model, String lang) throws Throwable {
        super.customShouldLoadRequired(timeout, model, lang);
        title.shouldHave(Condition.text("Aseguramiento de la calidad"), timeout);
    }
}
