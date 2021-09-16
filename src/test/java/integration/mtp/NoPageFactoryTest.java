package integration.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import integration.mtp.nopagefactorypom.MainFramePage;
import integration.mtp.nopagefactorypom.ServicesPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NoPageFactoryTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssurance() {
        MainFramePage mainFramePage = new MainFramePage();
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.getServicesLnk().hover();
        mainFramePage.mainMenu.getServicesPopUpQualityAssuranceLnk().click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() {
        MainFramePage mainFramePage = page(MainFramePage.class);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.getServicesLnk().hover();
        mainFramePage.mainMenu.getServicesPopUpQualityAssuranceLnk().click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }
}
