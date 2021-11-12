package integration.mtp;

import com.codeborne.selenide.Selenide;
import com.github.qky666.selenidepom.RequiredError;
import integration.mtp.widgetpom.MainFramePage;
import integration.mtp.widgetpom.ServicesPage;
import integration.mtp.widgetpom.ServicesRequiredErrorPage;
import integration.mtp.widgetpom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class WidgetTest extends BaseMtpTest {
    final MainFramePage mainFramePage = Selenide.page(MainFramePage.class);
    final ServicesPage servicesPage = Selenide.page(ServicesPage.class);
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = Selenide.page(ServicesShouldLoadRequiredErrorPage.class);
    final ServicesRequiredErrorPage servicesRequiredErrorPage = Selenide.page(ServicesRequiredErrorPage.class);

    @BeforeEach
    void acceptCookies() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.cookiesBanner.acceptCookiesIfDisplayed();
    }

    @Test
    void userNavigateToQualityAssurance() {
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        servicesPage.shouldLoadRequired();
        servicesPage.cookiesBanner.acceptCookiesIfDisplayed();
        Assertions.assertEquals(
                "div.cookie-notice-container/a#cn-accept-cookie",
                servicesPage.cookiesBanner.accept.getSearchCriteria()
        );
    }

    @Test
    void badSelectorError() {
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        servicesPage.shouldLoadRequired();
        servicesPage.cookiesBanner.acceptCookiesIfDisplayed();
        Assertions.assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();
        Assertions.assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() {
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        RequiredError error = Assertions.assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        Assertions.assertEquals(2, error.getErrors().size());
    }

    @Test
    void userForgotClick() {
        mainFramePage.mainMenu.services.hover();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(servicesPage.hasLoadedRequired());
        Assertions.assertFalse(servicesPage.hasLoadedRequiredWithTimeout(Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        Assertions.assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequiredWithTimeout(Duration.ofMillis(100)));
    }
}
