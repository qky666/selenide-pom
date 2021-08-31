package integration.mtp.pom.simple;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Page;
import es.qky.selenidepom.Required;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MtpCommonFramePage extends Page {
    @Required public SelenideElement mainMenu = $("div.custom-menu");
    @Required public SelenideElement mainMenuServicesLnk = mainMenu.$("li#servicios_menu");
    public SelenideElement mainMenuServicesPopUp = mainMenu.$("div.dropdown-servicios");
    public SelenideElement mainMenuServicesPopUpQualityAssuranceLnk = mainMenuServicesPopUp.$("a[data-principal='Aseguramiento de la calidad']");

    public SelenideElement badSelector = $("bad-selector");
}
