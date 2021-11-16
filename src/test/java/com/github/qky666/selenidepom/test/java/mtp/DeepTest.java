package com.github.qky666.selenidepom.test.java.mtp;

import com.github.qky666.selenidepom.java.RequiredError;
import com.github.qky666.selenidepom.test.java.mtp.deeppom.MainFramePage;
import com.github.qky666.selenidepom.test.java.mtp.deeppom.ServicesPage;
import com.github.qky666.selenidepom.test.java.mtp.deeppom.ServicesRequiredErrorPage;
import com.github.qky666.selenidepom.test.java.mtp.deeppom.ServicesShouldLoadRequiredErrorPage;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.ElementNotFound;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.*;

public class DeepTest extends BaseMtpTest {
    final MainFramePage mainFramePage = page(MainFramePage.class);
    final ServicesPage servicesPage = page(ServicesPage.class);
    final ServicesShouldLoadRequiredErrorPage servicesShouldLoadRequiredErrorPage = page(ServicesShouldLoadRequiredErrorPage.class);
    final ServicesRequiredErrorPage servicesRequiredErrorPage = page(ServicesRequiredErrorPage.class);

    @Test
    void userNavigateToQualityAssurance() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired();
        mainFramePage.mainMenu.servicesPopUp.qualityAssurance.click();
        assertEquals(
                "div.custom-menu/div.dropdown-servicios/a[data-principal='Aseguramiento de la calidad']",
                mainFramePage.mainMenu.servicesPopUp.qualityAssurance.getSearchCriteria()
        );

        servicesPage.shouldLoadRequired();
        servicesPage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUp.shouldLoadRequired(Duration.ofSeconds(2));
    }

    @Test
    void badSelectorError() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUp.qualityAssurance.click();

        servicesPage.shouldLoadRequired();
        assertThrows(ElementNotFound.class, () -> servicesPage.badSelector.click());
    }

    @Test
    void userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        mainFramePage.shouldLoadRequired(Duration.ofSeconds(3));
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUp.qualityAssurance.click();
        assertThrows(ElementShould.class, servicesShouldLoadRequiredErrorPage::shouldLoadRequired);
    }

    @Test
    void userNavigateToQualityAssuranceWithBadSelectorRequired() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services.hover();
        mainFramePage.mainMenu.servicesPopUp.qualityAssurance.click();

        RequiredError error = assertThrows(RequiredError.class, servicesRequiredErrorPage::shouldLoadRequired);
        assertEquals(1, error.getSuppressed().length);
    }

    @Test
    void userForgotClick() {
        mainFramePage.shouldLoadRequired();
        mainFramePage.mainMenu.services.hover();
        // User forgot to click Quality Assurance link

        assertFalse(servicesPage.hasLoadedRequired());
        assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)));
        assertThrows(RequiredError.class, servicesPage::shouldLoadRequired);
        assertThrows(RequiredError.class, () -> servicesPage.shouldLoadRequired(Duration.ofMillis(100)));
    }
}
