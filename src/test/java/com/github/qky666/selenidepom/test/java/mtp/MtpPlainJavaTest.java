package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import com.github.qky666.selenidepom.error.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.plainjavapom.ServicesShouldLoadRequiredErrorPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

public class MtpPlainJavaTest extends BaseMtpTest {
    final MainFramePage mainFramePage = new MainFramePage();
    final ServicesPage servicesPage = new ServicesPage();
    final ServicesRequiredErrorPage servicesRequiredErrorPage = new ServicesRequiredErrorPage();
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = new ServicesShouldLoadRequiredErrorPage();

    void acceptCookies() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.cookiesBanner.acceptCookies();
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssurance(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        servicesPage.shouldLoadRequired();
        Assertions.assertEquals(
                "div#cookie-law-info-bar/a#cookie_action_close_header",
                servicesPage.cookiesBanner.accept.getSearchCriteria()
        );
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void badSelectorError(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        servicesPage.shouldLoadRequired();
        Assertions.assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        Assertions.assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithBadSelectorRequired(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        RequiredError error = Assertions.assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        Assertions.assertEquals(2, error.getSuppressed().length);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userForgotClick(String browserConfig) {
        setUpBrowser(browserConfig);
        acceptCookies();
        mainFramePage.mainMenu.services.hover();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(servicesPage.hasLoadedRequired());
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        Assertions.assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
