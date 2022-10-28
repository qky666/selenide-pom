package com.github.qky666.selenidepom.test.java.mtp.pom.containerpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;

public class ServicesPage extends MainFramePage {

    // Fields
    @FindBy(css = "div.servicios-principal") @Required public SelenideElement principal;
    @FindBy(css = "h1.h2") @Required public SelenideElement title;
    @FindBy(css = "bad-selector") public SelenideElement badSelector;

    //Constructors
    @Override
    public void customShouldLoadRequired(Duration timeout, String pomVersion, String lang) throws Throwable {
        super.customShouldLoadRequired(timeout, pomVersion, lang);
        title.shouldHave(text("Aseguramiento de la calidad"), timeout);
    }
}
