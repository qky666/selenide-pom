package com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.error.RequiredError;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {
    @Required public final SelenideElement principal = $("div.servicios-principal");
    @Required public final SelenideElement title = $("h1.h2");

    @Override
    public void shouldLoadRequired(Duration timeout, String pomVersion) throws RequiredError {
        super.shouldLoadRequired(timeout, pomVersion);
        title.shouldHave(text("Aseguramiento de la calidad con error"), timeout);
    }
}
