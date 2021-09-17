package integration.mtp;

import integration.mtp.simplepom.MainFramePage;
import integration.mtp.simplepom.ServicesPage;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleTest extends BaseMtpTest {
    MainFramePage mainFramePage = page(MainFramePage.class);
    ServicesPage servicesPage = page(ServicesPage.class);

    @Test
    void userNavigateToQualityAssurance() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServicesLnk.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServicesLnk.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }
}
