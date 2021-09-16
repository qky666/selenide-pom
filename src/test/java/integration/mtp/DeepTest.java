package integration.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import integration.mtp.deeppom.MainFramePage;
import integration.mtp.deeppom.ServicesPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.codeborne.selenide.Selenide.page;

public class DeepTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssurance() {
        MainFramePage mainFramePage = page(MainFramePage.class);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired();
        mainFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
        servicesPage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired(Duration.ofSeconds(2));
    }

    @Test
    void badSelectorError() {
        MainFramePage mainFramePage = page(MainFramePage.class);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, () -> servicesPage.badSelector.click());
    }
}
