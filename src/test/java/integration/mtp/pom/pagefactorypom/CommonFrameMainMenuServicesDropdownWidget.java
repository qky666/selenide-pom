package integration.mtp.pom.pagefactorypom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.RequiredContainer;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

import javax.annotation.ParametersAreNonnullByDefault;


@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CommonFrameMainMenuServicesDropdownWidget extends ElementsContainer implements RequiredContainer {
    @Required @FindBy(css = "a[data-principal='Aseguramiento de la calidad']")
    public SelenideElement qualityAssuranceLnk;
}
