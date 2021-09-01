package integration.mtp.pom.simple;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Page;
import es.qky.selenidepom.Required;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class CommonFramePage extends Page {
    @Required
    public final SelenideElement mainMenu = $("div.custom-menu");
    @Required
    public final SelenideElement mainMenuServicesLnk = mainMenu.$("li#servicios_menu");
    public final SelenideElement mainMenuServicesPopUp = mainMenu.$("div.dropdown-servicios");
    public final SelenideElement mainMenuServicesPopUpQualityAssuranceLnk = mainMenuServicesPopUp.$("a[data-principal='Aseguramiento de la calidad']");
}
