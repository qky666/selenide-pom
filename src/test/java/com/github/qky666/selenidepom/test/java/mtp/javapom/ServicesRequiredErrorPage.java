package com.github.qky666.selenidepom.test.java.mtp.javapom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.RequiredError;

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
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        title.shouldHave(text("Aseguramiento de la calidad"));
    }
}
