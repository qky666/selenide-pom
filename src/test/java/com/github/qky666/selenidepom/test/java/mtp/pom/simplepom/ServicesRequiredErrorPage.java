package com.github.qky666.selenidepom.test.java.mtp.pom.simplepom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.error.RequiredError;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class ServicesRequiredErrorPage extends MainFramePage {
    @Required public SelenideElement principal = $("div.servicios-principal");
    @Required public final SelenideElement title = $("h1.h2");
    // badSelector generates an error in shouldLoadRequired
    @Required public final SelenideElement badSelector = $("bad-selector");


    @Override
    public void shouldLoadRequired(Duration timeout, String pomVersion) throws RequiredError {
        super.shouldLoadRequired(timeout, pomVersion);
        title.shouldHave(Condition.text("Aseguramiento de la calidad"), timeout);
    }
}
