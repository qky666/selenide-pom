package com.github.qky666.selenidepom.test.java.mtp;

import com.github.qky666.selenidepom.error.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.pom.simplepom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.pom.simplepom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.simplepom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.simplepom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

import static com.github.qky666.selenidepom.pom.LoadableKt.hasLoadedRequired;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;
import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.*;

public class ChromeSimpleTest extends BaseMtpTest {
    final MainFramePage mainFramePage = page(MainFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = page(ServicesShouldLoadRequiredErrorPage.class);
    final ServicesRequiredErrorPage servicesRequiredErrorPage = page(ServicesRequiredErrorPage.class);

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssurance(String browserConfig) {
        setUpBrowser(browserConfig);
        shouldLoadRequired(mainFramePage).mainMenuServices.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssurance.click();
        shouldLoadRequired(servicesPage);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void badSelectorError(String browserConfig) {
        setUpBrowser(browserConfig);
        shouldLoadRequired(mainFramePage).mainMenuServices.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssurance.click();

        shouldLoadRequired(servicesPage);
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError(String browserConfig) {
        setUpBrowser(browserConfig);
        shouldLoadRequired(mainFramePage, Duration.ofSeconds(3)).mainMenuServices.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssurance.click();
        assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesShouldLoadRequiredErrorPage));
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithBadSelectorRequired(String browserConfig) {
        setUpBrowser(browserConfig);
        shouldLoadRequired(mainFramePage).mainMenuServices.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssurance.click();

        RequiredError error = assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesRequiredErrorPage));
        assertEquals(1, error.getSuppressed().length);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userForgotClick(String browserConfig) {
        setUpBrowser(browserConfig);
        shouldLoadRequired(mainFramePage).mainMenuServices.hover();
        // User forgot to click Quality Assurance link

        assertFalse(hasLoadedRequired(servicesPage));
        assertFalse(hasLoadedRequired(servicesPage, Duration.ofMillis(100)));
        assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage));
        assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage, Duration.ofMillis(100)));
    }
}
