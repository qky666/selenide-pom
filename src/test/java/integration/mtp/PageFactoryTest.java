package integration.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import integration.mtp.pom.pagefactorypom.CommonFramePage;
import integration.mtp.pom.pagefactorypom.ServicesPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.codeborne.selenide.Selenide.page;

public class PageFactoryTest extends BaseMtpTest {
    final CommonFramePage commonFramePage = page(CommonFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);

    @Test
    void userNavigateToQualityAssurance() {
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUp.shouldLoadRequired();
        commonFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        servicesPage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUp.shouldLoadRequired(Duration.ofSeconds(2));
    }

    @Test
    void badSelectorError() {
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, () -> servicesPage.badSelector.click());
    }
}
