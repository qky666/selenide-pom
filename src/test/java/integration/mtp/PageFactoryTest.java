package integration.mtp;

import org.junit.jupiter.api.Test;
import integration.mtp.pom.pagefactory.commonframe.MtpCommonFramePage;
import integration.mtp.pom.pagefactory.services.ServicesPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;

public class PageFactoryTest extends BaseMtpTest {
    final MtpCommonFramePage mtpCommonFramePage = page(MtpCommonFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);

    @Test
    void userNavigateToServicesPage() {
        mtpCommonFramePage.shouldLoadRequired();

        mtpCommonFramePage.mainMenu.servicesLnk.hover();
        mtpCommonFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        servicesPage.titleTxt.shouldHave(text("Aseguramiento de la calidad"));
        servicesPage.mainMenu.servicesLnk.shouldBe(visible);
    }
}
