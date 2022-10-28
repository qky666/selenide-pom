package com.github.qky666.selenidepom.test.java.mtp.pom.constructorpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;

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
    public void customShouldLoadRequired(Duration timeout, String pomVersion, String lang) throws Throwable {
        super.customShouldLoadRequired(timeout, pomVersion, lang);
        title.shouldHave(text("Aseguramiento de la calidad"), timeout);
    }
}
