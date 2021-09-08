package integration.mtp;

import com.codeborne.selenide.ex.ElementShould;
import integration.mtp.pom.customshouldloadrequirederrorpom.CommonFramePage;
import integration.mtp.pom.customshouldloadrequirederrorpom.ServicesPage;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomShouldLoadRequiredErrorTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        CommonFramePage commonFramePage = page(CommonFramePage.class);
        commonFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        commonFramePage.mainMenu.servicesLnk.hover();
        commonFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        assertThrows(ElementShould.class, servicesPage::shouldLoadRequired);
    }
}
