package com.github.qky666.selenidepom.test.java.mtp.deeppom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.RequiredError;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {
    @Required
    @FindBy(css = "div.servicios-principal")
    public SelenideElement principal;
    @Required
    @FindBy(css = "h1.h2")
    public SelenideElement title;

    @Override
    public void shouldLoadRequired(Duration timeout) throws RequiredError {
        super.shouldLoadRequired(timeout);
        title.shouldHave(Condition.text("Aseguramiento de la calidad con error"), timeout);
    }
}