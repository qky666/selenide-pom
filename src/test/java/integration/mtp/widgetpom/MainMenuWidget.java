package integration.mtp.widgetpom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("unused")
public class MainMenuWidget extends ElementsContainer implements RequiredContainer {
    @Required
    @FindBy(css = "li#servicios_menu")
    public SelenideElement services;
    @FindBy(css = "div.dropdown-servicios")
    public SelenideElement servicesPopUp;
    @FindBy(css = "div.dropdown-servicios a[data-principal='Aseguramiento de la calidad']")
    public SelenideElement servicesPopUpQualityAssurance;
}
