package integration.mtp;

import integration.mtp.nopagefactorypom.MainFramePage;
import integration.mtp.nopagefactorypom.ServicesPage;
import integration.mtp.nopagefactorypom.ServicesRequiredErrorPage;
import integration.mtp.nopagefactorypom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NoPageFactoryTest extends BaseMtpTest {
    final MainFramePage mainFramePage = new MainFramePage();
    final ServicesPage servicesPage = new ServicesPage();
    final ServicesRequiredErrorPage servicesRequiredErrorPage = new ServicesRequiredErrorPage();
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = new ServicesShouldLoadRequiredErrorPage();

    @Test
    void userNavigateToQualityAssurance() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.getServicesLnk().hover();
        mainFramePage.mainMenu.getServicesPopUpQualityAssuranceLnk().click();

        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.getServicesLnk().hover();
        mainFramePage.mainMenu.getServicesPopUpQualityAssuranceLnk().click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() throws Throwable {
        mainFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        mainFramePage.mainMenu.getServicesLnk().hover();
        mainFramePage.mainMenu.getServicesPopUpQualityAssuranceLnk().click();
        assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.getServicesLnk().hover();
        mainFramePage.mainMenu.getServicesPopUpQualityAssuranceLnk().click();

        assertThrows(ElementNotFound.class, servicesRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userForgotClick() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.getServicesLnk().hover();
        // User forgot to click Quality Assurance link

        assertFalse(servicesPage.hasAlreadyLoadedRequired());
        assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        assertThrows(ElementNotFound.class, servicesPage::shouldLoadRequired);
        assertThrows(ElementNotFound.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
