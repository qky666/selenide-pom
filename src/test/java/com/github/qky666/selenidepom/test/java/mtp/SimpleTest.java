package com.github.qky666.selenidepom.test.java.mtp;

import com.github.qky666.selenidepom.java.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.simplepom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.simplepom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.simplepom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.simplepom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleTest extends BaseMtpTest {
    final MainFramePage mainFramePage = page(MainFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = page(ServicesShouldLoadRequiredErrorPage.class);
    final ServicesRequiredErrorPage servicesRequiredErrorPage = page(ServicesRequiredErrorPage.class);

    @Test
    void userNavigateToQualityAssurance() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServices.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssurance.click();

        servicesPage.shouldLoadRequired();
    }

    @Test
    void badSelectorError() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServices.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssurance.click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, servicesPage.badSelector::click);
    }

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        mainFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        mainFramePage.mainMenuServices.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssurance.click();
        assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServices.hover();
        mainFramePage.mainMenuServicesPopUpQualityAssurance.click();

        RequiredError error = assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        assertEquals(1, error.getSuppressed().length);
    }

    @Test
    void userForgotClick() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenuServices.hover();
        // User forgot to click Quality Assurance link

        assertFalse(servicesPage.hasLoadedRequired());
        assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
