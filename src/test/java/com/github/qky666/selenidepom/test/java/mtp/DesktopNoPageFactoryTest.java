package com.github.qky666.selenidepom.test.java.mtp;

import com.github.qky666.selenidepom.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.nopagefactorypom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.nopagefactorypom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.nopagefactorypom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.nopagefactorypom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class DesktopNoPageFactoryTest extends BaseMtpDesktopTest {
    final MainFramePage mainFramePage = new MainFramePage();
    final ServicesPage servicesPage = new ServicesPage();
    final ServicesRequiredErrorPage servicesRequiredErrorPage = new ServicesRequiredErrorPage();
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = new ServicesShouldLoadRequiredErrorPage();

    @Test
    void userNavigateToQualityAssurance() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services().hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services().hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        mainFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        mainFramePage.mainMenu.services().hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();
        assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services().hover();
        mainFramePage.mainMenu.servicesPopUpQualityAssurance().click();

        RequiredError error = assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        assertEquals(1, error.getSuppressed().length);
    }

    @Test
    void userForgotClick() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services().hover();
        // User forgot to click Quality Assurance link

        assertFalse(servicesPage.hasLoadedRequired());
        assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
