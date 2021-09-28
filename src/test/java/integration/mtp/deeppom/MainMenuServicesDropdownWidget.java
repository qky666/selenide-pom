package integration.mtp.deeppom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.RequiredContainer;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("unused")
public class MainMenuServicesDropdownWidget extends ElementsContainer implements RequiredContainer {
    @Required
    @FindBy(css = "a[data-principal='Aseguramiento de la calidad']")
    public SelenideElement qualityAssurance;
}
