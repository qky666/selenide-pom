package com.github.qky666.selenidepom.test.java.mtp;

import com.github.qky666.selenidepom.error.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.sleep;
import static com.github.qky666.selenidepom.pom.LoadableKt.hasLoadedRequired;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;
import static org.junit.jupiter.api.Assertions.*;

public class MtpNoPageFactoryTest extends BaseMtpTest {
    final MainFramePage mainFramePage = new MainFramePage();
    final ServicesPage servicesPage = new ServicesPage();
    final ServicesRequiredErrorPage servicesRequiredErrorPage = new ServicesRequiredErrorPage();
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = new ServicesShouldLoadRequiredErrorPage();

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssurance(String browserConfig) {
        setUpBrowser(browserConfig);
        // Sometimes first click is ignored
        shouldLoadRequired(mainFramePage).mainMenu.services().click();
        sleep(500);
        shouldLoadRequired(mainFramePage).mainMenu.services().click();

        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        shouldLoadRequired(servicesPage);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void badSelectorError(String browserConfig) {
        setUpBrowser(browserConfig);
        // Sometimes first click is ignored
        shouldLoadRequired(mainFramePage).mainMenu.services().click();
        sleep(500);
        shouldLoadRequired(mainFramePage).mainMenu.services().click();

        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        shouldLoadRequired(servicesPage);
        assertThrows(ElementNotFound.class, servicesPage.badSelector()::click);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError(String browserConfig) {
        setUpBrowser(browserConfig);
        // Sometimes first click is ignored
        shouldLoadRequired(mainFramePage, Duration.ofSeconds(3)).mainMenu.services().click();
        sleep(500);
        shouldLoadRequired(mainFramePage, Duration.ofSeconds(3)).mainMenu.services().click();

        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesShouldLoadRequiredErrorPage));
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithBadSelectorRequired(String browserConfig) {
        setUpBrowser(browserConfig);
        // Sometimes first click is ignored
        shouldLoadRequired(mainFramePage).mainMenu.services().click();
        sleep(500);
        shouldLoadRequired(mainFramePage).mainMenu.services().click();

        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        RequiredError error = assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesRequiredErrorPage));
        assertEquals(1, error.getSuppressed().length);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userForgotClick(String browserConfig) {
        setUpBrowser(browserConfig);
        // Sometimes first click is ignored
        shouldLoadRequired(mainFramePage).mainMenu.services().click();
        sleep(500);
        shouldLoadRequired(mainFramePage).mainMenu.services().click();
        // User forgot to click Quality Assurance link

        assertFalse(hasLoadedRequired(servicesPage));
        assertFalse(hasLoadedRequired(servicesPage, Duration.ofMillis(100)));
        assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage));
        assertThrows(RequiredError.class, () -> shouldLoadRequired(servicesPage, Duration.ofMillis(100)));
    }
}
