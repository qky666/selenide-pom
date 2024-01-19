package com.github.qky666.selenidepom.test.kotlin.demoqa

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.ex.ElementNotFound
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.RequiredError
import com.github.qky666.selenidepom.pom.hasLoadedRequired
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.pom.switch
import com.github.qky666.selenidepom.test.kotlin.demoqa.pom.badNestedFramesPage
import com.github.qky666.selenidepom.test.kotlin.demoqa.pom.nestedFramesPage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertFalse

private val logger = KotlinLogging.logger {}

class DemoqaTest {

    @BeforeEach
    fun beforeEach() {
        SPConfig.resetConfig()
        TestData.init("demoqa-prod")
    }

    @AfterEach
    fun afterEach() {
        SPConfig.quitDriver()
    }

    private fun setupSite(browserConfig: String = "chrome") {
        if (browserConfig.equals("chromeMobile", ignoreCase = true)) SPConfig.setupBasicMobileBrowser()
        else SPConfig.setupBasicDesktopBrowser(browserConfig)
        SPConfig.setDriver()
        Selenide.open(TestData.getString("data.url"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["chrome", "firefox", "chromeMobile"])
    fun happyTest(browserConfig: String) {
        setupSite(browserConfig)
        nestedFramesPage.shouldLoadRequired().framesWrapper.parentFrame.switch {
            it.body.shouldBe(visible)
            it.childFrame.switch { child ->
                child.text.shouldBe(visible)
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["chrome", "firefox", "chromeMobile"])
    fun badTest(browserConfig: String) {
        setupSite(browserConfig)
        val requiredError = assertThrows<RequiredError> { badNestedFramesPage.shouldLoadRequired() }
        assertEquals(1, requiredError.suppressed.size)
    }

    @ParameterizedTest
    @ValueSource(strings = ["chrome", "firefox", "chromeMobile"])
    fun switchTest(browserConfig: String) {
        setupSite(browserConfig)
        nestedFramesPage.shouldLoadRequired()
        // Default content
        nestedFramesPage.framesWrapper.shouldBe(visible)
        nestedFramesPage.framesWrapper.parentFrame.shouldBe(visible)
        nestedFramesPage.framesWrapper.parentFrame.body.shouldNotBe(visible)
        nestedFramesPage.framesWrapper.parentFrame.childFrame.shouldNotBe(visible)
        nestedFramesPage.framesWrapper.parentFrame.childFrame.text.shouldNotBe(visible)
        nestedFramesPage.framesWrapper.parentFrame.shouldLoadRequired()
        // Parent frame
        nestedFramesPage.framesWrapper.parentFrame.switch {
            nestedFramesPage.framesWrapper.shouldNotBe(visible)
            it.shouldNotBe(visible)
            it.body.shouldBe(visible)
            it.childFrame.shouldBe(visible)
            it.childFrame.text.shouldNotBe(visible)
            it.childFrame.shouldLoadRequired()
            assertFalse { it.hasLoadedRequired(Duration.ofSeconds(1)) }
            // Child frame
            it.childFrame.switch { child ->
                nestedFramesPage.framesWrapper.shouldNotBe(visible)
                it.shouldNotBe(visible)
                it.body.shouldNotBe(visible)
                child.shouldNotBe(visible)
                child.text.shouldBe(visible)
                assertFalse { it.hasLoadedRequired(Duration.ofSeconds(1)) }
                assertFalse { child.hasLoadedRequired(Duration.ofSeconds(1)) }
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["chrome", "firefox", "chromeMobile"])
    fun switchToChildTest(browserConfig: String) {
        setupSite(browserConfig)
        nestedFramesPage.shouldLoadRequired()
        assertThrows<ElementNotFound> {
            nestedFramesPage.framesWrapper.parentFrame.childFrame.switch { logger.debug { "This should not be run" } }
        }
    }
}
