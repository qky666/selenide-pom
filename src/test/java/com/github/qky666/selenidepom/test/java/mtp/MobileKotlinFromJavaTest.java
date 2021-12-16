package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import com.github.qky666.selenidepom.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.kotlinpom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.kotlinpom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.kotlinpom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.kotlinpom.ServicesShouldLoadRequiredErrorPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class MobileKotlinFromJavaTest extends BaseMtpMobileTest {
    final MainFramePage mainFramePage = new MainFramePage();
    final ServicesPage servicesPage = new ServicesPage();
    final ServicesRequiredErrorPage servicesRequiredErrorPage = new ServicesRequiredErrorPage();
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = new ServicesShouldLoadRequiredErrorPage();

    @BeforeEach
    void acceptCookies() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.getCookiesBanner().acceptCookies();
    }

    @Test
    void userNavigateToQualityAssurance() {
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().getServices().click();
        mainFramePage.getMobileMenu().getServicesQualityAssurance().click();

        servicesPage.shouldLoadRequired();
        Assertions.assertEquals(
                "div#cookie-law-info-bar/a#cookie_action_close_header",
                servicesPage.getCookiesBanner().getAccept().getSearchCriteria()
        );
    }

    @Test
    void badSelectorError() {
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().getServices().click();
        mainFramePage.getMobileMenu().getServicesQualityAssurance().click();

        servicesPage.shouldLoadRequired();
        Assertions.assertThrows(ElementNotFound.class, servicesPage.getBadSelector()::click);
    }

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().getServices().click();
        mainFramePage.getMobileMenu().getServicesQualityAssurance().click();

        Assertions.assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() {
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().getServices().click();
        mainFramePage.getMobileMenu().getServicesQualityAssurance().click();

        RequiredError error = Assertions.assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        Assertions.assertEquals(2, error.getSuppressed().length);
    }

    @Test
    void userForgotClick() {
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().getServices().click();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(servicesPage.hasLoadedRequired());
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        Assertions.assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
