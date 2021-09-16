package integration.mtp;

import integration.mtp.requirederrorpom.MainFramePage;
import integration.mtp.requirederrorpom.ServicesPage;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.ex.ElementNotFound;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequiredErrorTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() {
        MainFramePage mainFramePage = page(MainFramePage.class);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        assertThrows(ElementNotFound.class, servicesPage::shouldLoadRequired);
    }
}
