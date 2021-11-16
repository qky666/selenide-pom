package integration.mtp;

import com.github.qky666.selenidepom.kotlin.RequiredError;
import integration.mtp.lazypom.MainFramePage;
import integration.mtp.lazypom.ServicesPage;
import integration.mtp.lazypom.ServicesRequiredErrorPage;
import integration.mtp.lazypom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class LazyTest extends BaseMtpTest {
    final MainFramePage mainFramePage = new MainFramePage();
    final ServicesPage servicesPage = new ServicesPage();
    final ServicesRequiredErrorPage servicesRequiredErrorPage = new ServicesRequiredErrorPage();
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = new ServicesShouldLoadRequiredErrorPage();

    @BeforeEach
    void acceptCookies() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.getCookiesBanner().acceptCookiesIfDisplayed();
    }

    @Test
    void userNavigateToQualityAssurance() {
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        servicesPage.shouldLoadRequired();
        servicesPage.getCookiesBanner().acceptCookiesIfDisplayed();
        Assertions.assertEquals(
                "div.cookie-notice-container/a#cn-accept-cookie",
                servicesPage.getCookiesBanner().getAccept().getSearchCriteria()
        );
    }

    @Test
    void badSelectorError() {
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        servicesPage.shouldLoadRequired();
        servicesPage.getCookiesBanner().acceptCookiesIfDisplayed();
        Assertions.assertThrows(ElementNotFound.class, servicesPage.getBadSelector()::click);
    }

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        Assertions.assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() {
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        RequiredError error = Assertions.assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        Assertions.assertEquals(2, error.getErrors().size());
    }

    @Test
    void userForgotClick() {
        mainFramePage.getMainMenu().getServices().hover();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(servicesPage.hasLoadedRequired());
        Assertions.assertFalse(servicesPage.hasLoadedRequiredWithTimeout(Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        Assertions.assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequiredWithTimeout(Duration.ofMillis(100)));
    }
}
