package integration.mtp.pom.pagefactory.commonframe;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Container;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;

public class ServicesDropdownContainer extends Container {
    @FindBy(css = "a[data-principal='Aseguramiento de la calidad']") @Required public SelenideElement qualityAssuranceLnk;
}
