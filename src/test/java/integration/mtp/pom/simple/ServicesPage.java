package integration.mtp.pom.simple;

import com.codeborne.selenide.SelenideElement;
import es.qky.selenidepom.Required;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;


@ParametersAreNonnullByDefault
public class ServicesPage extends MtpCommonFramePage {
    @Required public SelenideElement principal = $("div.servicios-principal");
    @Required public SelenideElement titleTxt = $("h1.h2");
}
