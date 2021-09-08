package integration.mtp;

import integration.mtp.pom.requirederrorpom.CommonFramePage;
import integration.mtp.pom.requirederrorpom.ServicesPage;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.ex.ElementNotFound;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequiredErrorTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() {
        CommonFramePage commonFramePage = page(CommonFramePage.class);
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        assertThrows(ElementNotFound.class, servicesPage::shouldLoadRequired);
    }
}
