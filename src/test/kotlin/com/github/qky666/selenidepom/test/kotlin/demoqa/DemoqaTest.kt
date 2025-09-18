package com.github.qky666.selenidepom.test.kotlin.demoqa

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.ex.ElementNotFound
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.RequiredError
import com.github.qky666.selenidepom.pom.hasLoadedRequired
import com.github.qky666.selenidepom.pom.load
import com.github.qky666.selenidepom.pom.shouldLoadRequired
import com.github.qky666.selenidepom.pom.switch
import com.github.qky666.selenidepom.test.kotlin.demoqa.pom.BadNestedFramesPage
import com.github.qky666.selenidepom.test.kotlin.demoqa.pom.NestedFramesPage
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
    }

    @ParameterizedTest
    @ValueSource(strings = ["chrome", "firefox", "chromeMobile"])
    fun happyTest(browserConfig: String) {
        setupSite(browserConfig)
        Page.load(NestedFramesPage::class).shouldLoadRequired().framesWrapper.parentFrame.switch {
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
        val requiredError =
            assertThrows<RequiredError> { Page.load(BadNestedFramesPage::class).shouldLoadRequired() }
        assertEquals(1, requiredError.suppressed.size)
    }

    @ParameterizedTest
    @ValueSource(strings = ["chrome", "firefox", "chromeMobile"])
    fun switchTest(browserConfig: String) {
        setupSite(browserConfig)
        Page.load(NestedFramesPage::class).let { page ->
            page.shouldLoadRequired().framesWrapper.let { fw ->
                // Default content
                fw.shouldBe(visible)
                fw.parentFrame.shouldBe(visible)
                fw.parentFrame.body.shouldNotBe(visible)
                fw.parentFrame.childFrame.shouldNotBe(visible)
                fw.parentFrame.childFrame.text.shouldNotBe(visible)
                fw.parentFrame.shouldLoadRequired()
                // Parent frame
                fw.parentFrame.switch { iframe ->
                    page.framesWrapper.shouldNotBe(visible)
                    iframe.shouldNotBe(visible)
                    iframe.body.shouldBe(visible)
                    iframe.childFrame.shouldBe(visible)
                    iframe.childFrame.text.shouldNotBe(visible)
                    iframe.childFrame.shouldLoadRequired()
                    assertFalse { iframe.hasLoadedRequired(Duration.ofSeconds(1)) }
                    // Child frame
                    iframe.childFrame.switch { child ->
                        page.framesWrapper.shouldNotBe(visible)
                        iframe.shouldNotBe(visible)
                        iframe.body.shouldNotBe(visible)
                        child.shouldNotBe(visible)
                        child.text.shouldBe(visible)
                        assertFalse { iframe.hasLoadedRequired(Duration.ofSeconds(1)) }
                        assertFalse { child.hasLoadedRequired(Duration.ofSeconds(1)) }
                    }
                }
            }
        }

    }

    @ParameterizedTest
    @ValueSource(strings = ["chrome", "firefox", "chromeMobile"])
    fun switchToChildTest(browserConfig: String) {
        setupSite(browserConfig)
        Page.load(NestedFramesPage::class).let {
            it.shouldLoadRequired()
            assertThrows<ElementNotFound> {
                it.framesWrapper.parentFrame.childFrame.switch { logger.debug { "This should not be run" } }
            }
        }
    }
}
