package integration.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import integration.mtp.pom.pagefactorypom.CommonFramePage;
import integration.mtp.pom.pagefactorypom.ServicesPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.codeborne.selenide.Selenide.page;

public class PageFactoryTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssurance() {
        CommonFramePage commonFramePage = page(CommonFramePage.class);
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUp.shouldLoadRequired();
        commonFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
        servicesPage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUp.shouldLoadRequired(Duration.ofSeconds(2));
    }

    @Test
    void badSelectorError() {
        CommonFramePage commonFramePage = page(CommonFramePage.class);
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUp.qualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, () -> servicesPage.badSelector.click());
    }
}
