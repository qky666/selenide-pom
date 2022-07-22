package com.github.qky666.selenidepom.test.java.mtp.pom.constructorpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.error.RequiredError;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ServicesRequiredErrorPage extends MainFramePage {
    @Required public final SelenideElement principal = $("div.servicios-principal");
    @Required public final SelenideElement title = $("h1.h2");
    // badSelector generates an error in shouldLoadRequired
    @Required public final SelenideElement badSelector = $("bad-selector");
    @Required public final SelenideElement otherBadSelector = $("other-bad-selector");

    @Override
    public void shouldLoadRequired(Duration timeout, String pomVersion) throws RequiredError {
        super.shouldLoadRequired(timeout, pomVersion);
        title.shouldHave(text("Aseguramiento de la calidad"));
    }
}
