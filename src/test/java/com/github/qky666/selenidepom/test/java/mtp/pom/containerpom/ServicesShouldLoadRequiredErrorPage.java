package com.github.qky666.selenidepom.test.java.mtp.pom.containerpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;

public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {

    // Fields
    @FindBy(css = "div.servicios-principal") @Required public SelenideElement principal;
    @FindBy(css = "h1.h2") @Required public SelenideElement title;

    // Methods
    @Override
    public void customShouldLoadRequired(Duration timeout, String pomVersion) throws Throwable {
        super.customShouldLoadRequired(timeout, pomVersion);
        title.shouldHave(text("Aseguramiento de la calidad con error"), timeout);
    }
}
