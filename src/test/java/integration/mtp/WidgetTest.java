package integration.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import integration.mtp.pom.widgetpom.CommonFramePage;
import integration.mtp.pom.widgetpom.ServicesPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WidgetTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssurance() {
        CommonFramePage commonFramePage = page(CommonFramePage.class);
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() {
        CommonFramePage commonFramePage = page(CommonFramePage.class);
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }
}
