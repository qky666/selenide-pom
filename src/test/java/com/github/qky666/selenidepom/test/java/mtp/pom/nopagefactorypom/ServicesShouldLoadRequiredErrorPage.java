package com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {
    @Required
    public final SelenideElement principal() {return $("div.servicios-principal");}

    @Required
    public final SelenideElement title() {return $("h1.h2");}

    @Override
    public void customShouldLoadRequired(Duration timeout, String pomVersion, String lang) throws Throwable {
        super.customShouldLoadRequired(timeout, pomVersion, lang);
        title().shouldHave(text("Aseguramiento de la calidad con error"), timeout);
    }
}
