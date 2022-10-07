package com.github.qky666.selenidepom.test.java.mtp.pom.javapom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ServicesRequiredErrorPage extends MainFramePage {

    // Fields
    @Getter(lazy = true) @Required private final SelenideElement principal = $("div.servicios-principal");
    @Getter(lazy = true) @Required private final SelenideElement title = $("h1.h2");
    // badSelector generates an error in shouldLoadRequired
    @Getter(lazy = true) @Required private final SelenideElement badSelector = $("bad-selector");
    @Getter(lazy = true) @Required private final SelenideElement otherBadSelector = $("other-bad-selector");

    // Methods
    @Override
    public void customShouldLoadRequired(Duration timeout, String pomVersion) throws Throwable {
        super.customShouldLoadRequired(timeout, pomVersion);
        getTitle().shouldHave(text("Aseguramiento de la calidad"), timeout);
    }
}
