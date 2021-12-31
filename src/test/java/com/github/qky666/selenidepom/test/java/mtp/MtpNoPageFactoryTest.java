package com.github.qky666.selenidepom.test.java.mtp;

import com.github.qky666.selenidepom.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

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
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services().hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        servicesPage.shouldLoadRequired();
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void badSelectorError(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services().hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        mainFramePage.mainMenu.services().hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();
        assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userNavigateToQualityAssuranceWithBadSelectorRequired(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services().hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        RequiredError error = assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        assertEquals(1, error.getSuppressed().length);
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigSource")
    void userForgotClick(String browserConfig) {
        setUpBrowser(browserConfig);
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services().hover();
        // User forgot to click Quality Assurance link

        assertFalse(servicesPage.hasLoadedRequired());
        assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
