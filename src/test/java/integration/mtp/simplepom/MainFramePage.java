package integration.mtp.simplepom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage implements RequiredContainer {
    @Required
    public final SelenideElement mainMenu = $("div.custom-menu");
    @Required
    public final SelenideElement mainMenuServices = mainMenu.$("li#servicios_menu");
    public final SelenideElement mainMenuServicesPopUp = mainMenu.$("div.dropdown-servicios");
    public final SelenideElement mainMenuServicesPopUpQualityAssurance = mainMenuServicesPopUp.$("a[data-principal='Aseguramiento de la calidad']");
}
