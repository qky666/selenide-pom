package integration.mtp;

import com.codeborne.selenide.ex.ElementShould;
import integration.mtp.customshouldloadrequirederrorpom.MainFramePage;
import integration.mtp.customshouldloadrequirederrorpom.ServicesPage;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomShouldLoadRequiredErrorTest extends BaseMtpTest {
    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        MainFramePage mainFramePage = page(MainFramePage.class);
        mainFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        mainFramePage.mainMenu.servicesLnk.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssuranceLnk.click();

        ServicesPage servicesPage = page(ServicesPage.class);
        assertThrows(ElementShould.class, servicesPage::shouldLoadRequired);
    }
}
