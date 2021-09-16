package integration.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import integration.mtp.simplepom.MainFramePage;
import integration.mtp.simplepom.ServicesPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssurance() {
        MainFramePage mainFramePage = page(MainFramePage.class);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServicesLnk.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() {
        MainFramePage mainFramePage = page(MainFramePage.class);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServicesLnk.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }
}
