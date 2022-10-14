package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.ex.ElementNotFound;
import com.github.qky666.selenidepom.pom.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.pom.containerpom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.pom.containerpom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.containerpom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.containerpom.ServicesShouldLoadRequiredErrorPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.page;
import static com.github.qky666.selenidepom.pom.LoadableKt.hasLoadedRequired;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;

public class MtpContainerTest extends BaseMtpTest {
    final MainFramePage mainFramePage = page(MainFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);
    final ServicesRequiredErrorPage servicesRequiredErrorPage = page(ServicesRequiredErrorPage.class);
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = page(ServicesShouldLoadRequiredErrorPage.class);

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssurance(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        shouldLoadRequired(servicesPage);
        Assertions.assertEquals("div#cookie-law-info-bar/a#cookie_action_close_header", servicesPage.cookiesBanner.accept.getSearchCriteria());
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void badSelectorError(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        shouldLoadRequired(servicesPage);
        Assertions.assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesShouldLoadRequiredErrorPage));
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithBadSelectorRequired(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance.click();

        RequiredError error = Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesRequiredErrorPage));
        Assertions.assertEquals(2, error.getSuppressed().length);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userForgotClick(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.acceptCookies();
        mainFramePage.mainMenu.services.hover();
        // User forgot to click Quality Assurance link

        Assertions.assertFalse(hasLoadedRequired(servicesPage));
        Assertions.assertFalse(hasLoadedRequired(servicesPage, Duration.ofMillis(100)));
        Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage));
        Assertions.assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage, Duration.ofMillis(100)));
    }
}
