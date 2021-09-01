package integration.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import integration.mtp.pom.simple.CommonFramePage;
import integration.mtp.pom.simple.ServicesPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleTest extends BaseMtpTest {
    final CommonFramePage commonFramePage = page(CommonFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);

    @Test
    void userNavigateToQualityAssurance() {
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenuServicesLnk.hover();
        commonFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() {
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenuServicesLnk.hover();
        commonFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }
}
