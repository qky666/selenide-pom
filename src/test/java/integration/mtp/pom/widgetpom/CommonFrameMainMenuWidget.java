package integration.mtp.pom.widgetpom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;


@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CommonFrameMainMenuWidget extends ElementsContainer implements RequiredContainer {
    @Required public final SelenideElement servicesLnk = $("li#servicios_menu");
    public final SelenideElement servicesPopUp = $("div.dropdown-servicios");
    public final SelenideElement servicesPopUpQualityAssuranceLnk = servicesPopUp.$("a[data-principal='Aseguramiento de la calidad']");
}
