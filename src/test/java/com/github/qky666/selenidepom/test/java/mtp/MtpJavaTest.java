package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import com.github.qky666.selenidepom.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.kotlinpom.ServicesShouldLoadRequiredErrorPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

public class MtpJavaTest extends BaseMtpTest {
    final MainFramePage mainFramePage = new MainFramePage();
    final ServicesPage servicesPage = new ServicesPage();
    final ServicesRequiredErrorPage servicesRequiredErrorPage = new ServicesRequiredErrorPage();
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = new ServicesShouldLoadRequiredErrorPage();

    void acceptCookies() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.getCookiesBanner().acceptCookies();
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        servicesPage.shouldLoadRequired();
        Assertions.assertEquals("div#cookie-law-info-bar/a#cookie_action_close_header", servicesPage.getCookiesBanner().getAccept().getSearchCriteria());
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void badSelectorErrorDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        servicesPage.shouldLoadRequired();
        Assertions.assertThrows(ElementNotFound.class, servicesPage.getBadSelector()::click);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        Assertions.assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithBadSelectorRequiredDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        RequiredError error = Assertions.assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        Assertions.assertEquals(2, error.getSuppressed().length);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userForgotClickDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(servicesPage.hasLoadedRequired());
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        Assertions.assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void userNavigateToQualityAssuranceMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().shouldBeCollapsed();
        mainFramePage.getMobileMenu().getServices().click();
        mainFramePage.getMobileMenu().getServicesQualityAssurance().shouldBe(Condition.visible).click();

        servicesPage.shouldLoadRequired();
        Assertions.assertEquals(
                "div#cookie-law-info-bar/a#cookie_action_close_header",
                servicesPage.getCookiesBanner().getAccept().getSearchCriteria()
        );
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void badSelectorErrorMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().shouldBeCollapsed();
        mainFramePage.getMobileMenu().getServices().click();
        mainFramePage.getMobileMenu().getServicesQualityAssurance().shouldBe(Condition.visible).click();

        servicesPage.shouldLoadRequired();
        Assertions.assertThrows(ElementNotFound.class, servicesPage.getBadSelector()::click);
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().shouldBeCollapsed();
        mainFramePage.getMobileMenu().getServices().click();
        mainFramePage.getMobileMenu().getServicesQualityAssurance().shouldBe(Condition.visible).click();

        Assertions.assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void userNavigateToQualityAssuranceWithBadSelectorRequiredMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().shouldBeCollapsed();
        mainFramePage.getMobileMenu().getServices().click();
        mainFramePage.getMobileMenu().getServicesQualityAssurance().shouldBe(Condition.visible).click();

        RequiredError error = Assertions.assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        Assertions.assertEquals(2, error.getSuppressed().length);
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void userForgotClickMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        mainFramePage.getMobileMenu().shouldLoadRequired();
        mainFramePage.getMobileMenu().shouldBeCollapsed();
        mainFramePage.getMobileMenu().getServices().click();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(servicesPage.hasLoadedRequired());
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        Assertions.assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
