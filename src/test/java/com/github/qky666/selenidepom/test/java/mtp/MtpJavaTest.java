package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.github.qky666.selenidepom.pom.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.pom.javapom.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

import static com.github.qky666.selenidepom.pom.LoadableKt.hasLoadedRequired;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;

public class MtpJavaTest extends BaseMtpTest {
    final MainFramePage mainFramePage = new MainFramePage();
    final ServicesPage servicesPage = new ServicesPage();
    final ServicesRequiredErrorPage servicesRequiredErrorPage = new ServicesRequiredErrorPage();
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = new ServicesShouldLoadRequiredErrorPage();

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        shouldLoadRequired(servicesPage);
        Assertions.assertEquals("div#cookie-law-info-bar/a#cookie_action_close_header", servicesPage.getCookiesBanner().getAccept().getSearchCriteria());
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void badSelectorErrorDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        shouldLoadRequired(servicesPage);
        Assertions.assertThrows(ElementNotFound.class, servicesPage.getBadSelector()::click);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesShouldLoadRequiredErrorPage));
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithBadSelectorRequiredDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        mainFramePage.getMainMenu().getServicesPopUpQualityAssurance().click();

        RequiredError error = Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesRequiredErrorPage));
        Assertions.assertEquals(2, error.getSuppressed().length);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userForgotClickDesktop(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMainMenu().getServices().hover();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(hasLoadedRequired(servicesPage));
        Assertions.assertFalse(hasLoadedRequired(servicesPage, Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage));
        Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage, Duration.ofMillis(100)));
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void userNavigateToQualityAssuranceMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        MobileMenuWidget mobileMenu = mainFramePage.getMobileMenu();
        shouldLoadRequired(mobileMenu);
        mobileMenu.shouldBeCollapsed();
        mobileMenu.getServices().click();
        mobileMenu.getServicesQualityAssurance().shouldBe(Condition.visible).click();

        shouldLoadRequired(servicesPage);
        Assertions.assertEquals("div#cookie-law-info-bar/a#cookie_action_close_header", servicesPage.getCookiesBanner().getAccept().getSearchCriteria());
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void badSelectorErrorMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        MobileMenuWidget mobileMenu = mainFramePage.getMobileMenu();
        shouldLoadRequired(mobileMenu);
        mobileMenu.shouldBeCollapsed();
        mobileMenu.getServices().click();
        mobileMenu.getServicesQualityAssurance().shouldBe(Condition.visible).click();

        shouldLoadRequired(servicesPage);
        Assertions.assertThrows(ElementNotFound.class, servicesPage.getBadSelector()::click);
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredErrorMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        MobileMenuWidget mobileMenu = mainFramePage.getMobileMenu();
        shouldLoadRequired(mobileMenu);
        mobileMenu.shouldBeCollapsed();
        mobileMenu.getServices().click();
        mobileMenu.getServicesQualityAssurance().shouldBe(Condition.visible).click();

        Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesShouldLoadRequiredErrorPage));
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void userNavigateToQualityAssuranceWithBadSelectorRequiredMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        MobileMenuWidget mobileMenu = mainFramePage.getMobileMenu();
        shouldLoadRequired(mobileMenu);
        mobileMenu.shouldBeCollapsed();
        mobileMenu.getServices().click();
        mobileMenu.getServicesQualityAssurance().shouldBe(Condition.visible).click();

        RequiredError error = Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesRequiredErrorPage));
        Assertions.assertEquals(2, error.getSuppressed().length);
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigSource")
    void userForgotClickMobile(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.getMobileMenuButton().click();
        MobileMenuWidget mobileMenu = mainFramePage.getMobileMenu();
        shouldLoadRequired(mobileMenu);
        mobileMenu.shouldBeCollapsed();
        mobileMenu.getServices().click();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(hasLoadedRequired(servicesPage));
        Assertions.assertFalse(hasLoadedRequired(servicesPage, Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage));
        Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage, Duration.ofMillis(100)));
    }
}
