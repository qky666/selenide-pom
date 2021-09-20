package integration.mtp;

import integration.mtp.deeppom.MainFramePage;
import integration.mtp.deeppom.ServicesPage;
import integration.mtp.deeppom.ServicesRequiredErrorPage;
import integration.mtp.deeppom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.codeborne.selenide.Selenide.page;

public class DeepTest extends BaseMtpTest {
    final MainFramePage mainFramePage = page(MainFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = page(ServicesShouldLoadRequiredErrorPage.class);
    final ServicesRequiredErrorPage servicesRequiredErrorPage = page(ServicesRequiredErrorPage.class);

    @Test
    void userNavigateToQualityAssurance() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired();
        mainFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        servicesPage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired(Duration.ofSeconds(2));
    }

    @Test
    void badSelectorError() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, () -> servicesPage.badSelector.click());
    }

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() throws Throwable {
        mainFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();
        assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        assertThrows(ElementNotFound.class, servicesRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userForgotClick() throws Throwable {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        // User forgot to click Quality Assurance link

        assertFalse(servicesPage.hasAlreadyLoadedRequired());
        assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        assertThrows(ElementNotFound.class, servicesPage::shouldLoadRequired);
        assertThrows(ElementNotFound.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
