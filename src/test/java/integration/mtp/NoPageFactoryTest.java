package integration.mtp;

import integration.mtp.nopagefactorypom.MainFramePage;
import integration.mtp.nopagefactorypom.ServicesPage;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NoPageFactoryTest extends BaseMtpTest {
    MainFramePage mainFramePage = new MainFramePage();
    ServicesPage servicesPage = new ServicesPage();

    @Test
    void userNavigateToQualityAssurance() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.getServicesLnk().hover();
        mainFramePage.mainMenu.getServicesPopUpQualityAssuranceLnk().click();

        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.getServicesLnk().hover();
        mainFramePage.mainMenu.getServicesPopUpQualityAssuranceLnk().click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }
}
