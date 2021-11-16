package com.github.qky666.selenidepom.test.java.mtp.widgetpom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.RequiredError;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;

public class ServicesPage extends MainFramePage {
    @Required
    @FindBy(css = "div.servicios-principal")
    public SelenideElement principal;
    @Required
    @FindBy(css = "h1.h2")
    public SelenideElement title;
    @FindBy(css = "bad-selector")
    public SelenideElement badSelector;

    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        title.shouldHave(text("Aseguramiento de la calidad"), timeout);
    }
}
