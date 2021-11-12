package integration.mtp.javapom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.RequiredError;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ServicesShouldLoadRequiredErrorPage extends MainFramePage {
    @Required public final SelenideElement principal = $("div.servicios-principal");
    @Required public final SelenideElement title = $("h1.h2");

    @Override
    public void shouldLoadRequiredWithTimeout(Duration timeout) throws RequiredError {
        super.shouldLoadRequiredWithTimeout(timeout);
        title.shouldHave(text("Aseguramiento de la calidad con error"), timeout);
    }
}
