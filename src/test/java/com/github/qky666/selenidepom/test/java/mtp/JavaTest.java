package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import com.github.qky666.selenidepom.java.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.javapom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.javapom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.javapom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.javapom.ServicesShouldLoadRequiredErrorPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class JavaTest extends BaseMtpTest {
    final MainFramePage mainFramePage = new MainFramePage();
    final ServicesPage servicesPage = new ServicesPage();
    final ServicesRequiredErrorPage servicesRequiredErrorPage = new ServicesRequiredErrorPage();
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = new ServicesShouldLoadRequiredErrorPage();

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
        Assertions.assertEquals(2, error.getSuppressed().length);
    }

    @Test
    void userForgotClick() {
        mainFramePage.mainMenu.services.hover();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(servicesPage.hasLoadedRequired());
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        Assertions.assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
