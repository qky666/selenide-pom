package integration.mtp;

import integration.mtp.pom.requirederrorpom.CommonFramePage;
import integration.mtp.pom.requirederrorpom.ServicesPage;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.ex.ElementNotFound;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequiredErrorTest extends BaseMtpTest {
    final CommonFramePage commonFramePage = page(CommonFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() {
        commonFramePage.shouldLoadRequired();
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        assertThrows(ElementNotFound.class, servicesPage::shouldLoadRequired);
    }
}
