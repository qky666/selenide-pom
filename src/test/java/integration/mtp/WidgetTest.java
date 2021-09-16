package integration.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import integration.mtp.widgetpom.MainFramePage;
import integration.mtp.widgetpom.ServicesPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WidgetTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssurance() {
        MainFramePage mainFramePage = page(MainFramePage.class);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() {
        MainFramePage mainFramePage = page(MainFramePage.class);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }
}
