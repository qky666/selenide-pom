package integration.mtp;

import integration.mtp.pom.simple.MtpCommonFramePage;
import integration.mtp.pom.simple.ServicesPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;

public class SimpleTest extends BaseMtpTest {
    final MtpCommonFramePage mtpCommonFramePage = page(MtpCommonFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);

    @Test
    void userNavigateToServicesPage() {
        mtpCommonFramePage.shouldLoadRequired();

        mtpCommonFramePage.mainMenuServicesLnk.hover();
        mtpCommonFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        servicesPage.titleTxt.shouldHave(text("Aseguramiento de la calidad"));
        servicesPage.mainMenuServicesLnk.shouldBe(visible);
    }
}
