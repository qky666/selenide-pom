package integration.mtp;

import com.codeborne.selenide.ex.ElementShould;
import integration.mtp.pom.customshouldloadrequirederrorpom.CommonFramePage;
import integration.mtp.pom.customshouldloadrequirederrorpom.ServicesPage;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomShouldLoadRequiredErrorTest extends BaseMtpTest {
    final CommonFramePage commonFramePage = page(CommonFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        commonFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        assertThrows(ElementShould.class, servicesPage::shouldLoadRequired);
    }
}
