package integration.mtp;

import integration.mtp.deeppom.MainFramePage;
import integration.mtp.deeppom.ServicesPage;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.codeborne.selenide.Selenide.page;

public class DeepTest extends BaseMtpTest {
    MainFramePage mainFramePage = page(MainFramePage.class);
    ServicesPage servicesPage = page(ServicesPage.class);

    @Test
    void userNavigateToQualityAssurance() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired();
        mainFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        servicesPage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired(Duration.ofSeconds(2));
    }

    @Test
    void badSelectorError() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, () -> servicesPage.badSelector.click());
    }
}
