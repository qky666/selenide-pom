package com.github.qky666.selenidepom.test.kotlin.mtp

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.codeborne.selenide.ex.ElementNotFound
import com.codeborne.selenide.ex.ElementShould
import com.github.qky666.selenidepom.RequiredError
import org.junit.jupiter.api.Assertions
import java.time.Duration
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.MainFramePage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.ServicesPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.ServicesRequiredErrorPage
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.ServicesShouldLoadRequiredErrorPage

class MobileKotlinTest : BaseMtpMobileTest() {
    val mainFramePage = MainFramePage()
    val servicesPage = ServicesPage()
    val servicesRequiredErrorPage = ServicesRequiredErrorPage()
    val servicesShouldLoadRequiredErrorPage = ServicesShouldLoadRequiredErrorPage()

    @BeforeEach
    fun acceptCookies() {
        mainFramePage.shouldLoadRequired()
        mainFramePage.cookiesBanner.acceptCookies()
    }

    @Test
    fun userNavigateToQualityAssurance() {
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        mainFramePage.mobileMenu.servicesQualityAssurance.click()
        servicesPage.shouldLoadRequired()
        Assertions.assertEquals(
            "div#cookie-law-info-bar/a#cookie_action_close_header",
            servicesPage.cookiesBanner.accept.searchCriteria
        )
    }

    @Test
    fun badSelectorError() {
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        mainFramePage.mobileMenu.servicesQualityAssurance.click()
        servicesPage.shouldLoadRequired()
        Assertions.assertThrows(ElementNotFound::class.java) { servicesPage.badSelector.click() }
    }

    @Test
    fun userNavigateToQualityAssuranceWithCustomShouldLoadRequiredError() {
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        mainFramePage.mobileMenu.servicesQualityAssurance.click()
        Assertions.assertThrows(ElementShould::class.java) { servicesShouldLoadRequiredErrorPage.shouldLoadRequired() }
    }

    @Test
    fun userNavigateToQualityAssuranceWithBadSelectorRequired() {
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        mainFramePage.mobileMenu.servicesQualityAssurance.click()
        val error =
            Assertions.assertThrows(RequiredError::class.java) { servicesRequiredErrorPage.shouldLoadRequired() }
        Assertions.assertEquals(2, error.suppressed.size)
    }

    @Test
    fun userForgotClick() {
        mainFramePage.mobileMenuButton.click()
        mainFramePage.mobileMenu.shouldLoadRequired()
        mainFramePage.mobileMenu.services.click()
        // User forgot to click Quality Assurance link
        Assertions.assertFalse(servicesPage.hasLoadedRequired())
        Assertions.assertFalse(servicesPage.hasLoadedRequired(Duration.ofMillis(100)))
        Assertions.assertThrows(RequiredError::class.java) { servicesPage.shouldLoadRequired() }
        Assertions.assertThrows(RequiredError::class.java) { servicesPage.shouldLoadRequired(Duration.ofMillis(100)) }
    }
}