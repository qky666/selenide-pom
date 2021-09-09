package integration.mtp.pom.simplepom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class CommonFramePage implements RequiredContainer {
    @Required
    public final SelenideElement mainMenu = $("div.custom-menu");
    @Required
    public final SelenideElement mainMenuServicesLnk = mainMenu.$("li#servicios_menu");
    public final SelenideElement mainMenuServicesPopUp = mainMenu.$("div.dropdown-servicios");
    public final SelenideElement mainMenuServicesPopUpQualityAssuranceLnk = mainMenuServicesPopUp.$("a[data-principal='Aseguramiento de la calidad']");
}
