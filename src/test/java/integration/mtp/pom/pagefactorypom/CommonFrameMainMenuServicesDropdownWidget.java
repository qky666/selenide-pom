package integration.mtp.pom.pagefactorypom;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Widget;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CommonFrameMainMenuServicesDropdownWidget extends Widget {
    @FindBy(css = "a[data-principal='Aseguramiento de la calidad']")
    @Required
    public SelenideElement qualityAssuranceLnk;
}
