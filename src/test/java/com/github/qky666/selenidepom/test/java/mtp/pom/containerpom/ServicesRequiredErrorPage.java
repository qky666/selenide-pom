package com.github.qky666.selenidepom.test.java.mtp.pom.containerpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Required;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;

public class ServicesRequiredErrorPage extends MainFramePage {

    // Fields
    @FindBy(css = "div.servicios-principal") @Required public SelenideElement principal;
    @FindBy(css = "h1.h2") @Required public SelenideElement title;
    // badSelector generates an error in shouldLoadRequired
    @FindBy(css = "bad-selector") @Required public SelenideElement badSelector;
    @FindBy(css = "other-bad-selector") @Required public SelenideElement otherBadSelector;

    // Methods
    @Override
    public void customShouldLoadRequired(Duration timeout, String model, String lang) throws Throwable {
        super.customShouldLoadRequired(timeout, model, lang);
        title.shouldHave(text("Aseguramiento de la calidad"), timeout);
    }
}
