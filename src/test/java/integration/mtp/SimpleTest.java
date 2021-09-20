package integration.mtp;

import integration.mtp.simplepom.MainFramePage;
import integration.mtp.simplepom.ServicesPage;
import integration.mtp.simplepom.ServicesRequiredErrorPage;
import integration.mtp.simplepom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleTest extends BaseMtpTest {
    final MainFramePage mainFramePage = page(MainFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = page(ServicesShouldLoadRequiredErrorPage.class);
    final ServicesRequiredErrorPage servicesRequiredErrorPage = page(ServicesRequiredErrorPage.class);

    @Test
    void userNavigateToQualityAssurance() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServicesLnk.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServicesLnk.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() throws Throwable {
        mainFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        mainFramePage.mainMenuServicesLnk.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();
        assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServicesLnk.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssuranceLnk.click();

        assertThrows(ElementNotFound.class, servicesRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userForgotClick() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServicesLnk.hover();
        // User forgot to click Quality Assurance link

        assertFalse(servicesPage.hasAlreadyLoadedRequired());
        assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        assertThrows(ElementNotFound.class, servicesPage::shouldLoadRequired);
        assertThrows(ElementNotFound.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
