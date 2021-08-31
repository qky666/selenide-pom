package integration.mtp.pom.pagefactory.services;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;
import org.openqa.selenium.support.FindBy;
import integration.mtp.pom.pagefactory.commonframe.MtpCommonFramePage;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class ServicesPage extends MtpCommonFramePage {
    @FindBy(css = "div.servicios-principal") @Required public SelenideElement principal;
    @FindBy(css = "h1.h2") @Required public SelenideElement titleTxt;
}
