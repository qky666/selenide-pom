package com.github.qky666.selenidepom.test.kotlin.tiddlywiki

import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertNotNull

class TiddlywikiTest {

    companion object {
        @JvmStatic
        fun desktopBrowserConfigAndLangSource(): List<Arguments> {
            return listOf(
                Arguments.of("chrome", "es"),
                Arguments.of("chrome", "en"),
                Arguments.of("firefox", "es"),
                Arguments.of("chrome", "en")
            )
        }

        @JvmStatic
        fun mobileBrowserConfigAndLangSource(): List<Arguments> {
            return listOf(
                Arguments.of("chromeMobile", "es"),
                Arguments.of("chromeMobile", "en")
            )
        }
    }

    @BeforeEach
    fun beforeEach() {
        SPConfig.resetConfig()

        // Additional test for output in TestData
        TestData.init("tiddlywiki-prod")
        TestData.output["threadId"] = Thread.currentThread().id
    }

    @AfterEach
    fun afterEach() {
        SPConfig.quitCurrentThreadDriver()
        // Additional test for output in TestData
        Assertions.assertEquals(TestData.output["threadId"].toString(), Thread.currentThread().id.toString())
    }

    private fun setupSite(browserConfig: String, lang: String = "en") {
        if (browserConfig.equals("chromeMobile", ignoreCase = true)) {
            SPConfig.setupBasicMobileBrowser()
        } else {
            SPConfig.setupBasicDesktopBrowser(browserConfig)
        }
        SPConfig.setCurrentThreadDriver()
        SPConfig.lang = lang
        val relativeUrl = TestData.input.getProperty("data.input.relativeUrl")
        val url = Thread.currentThread().contextClassLoader.getResource(relativeUrl)
        assertNotNull(url, "URL not found")
        Selenide.open(url)
        changeSiteLanguageIfNeeded()
    }

    private fun changeSiteLanguageIfNeeded(newLang: String = SPConfig.lang, currentLang: String = "es") {
        if (!newLang.contentEquals(currentLang, true)) {
            // TODO: Change site language if needed
            throw RuntimeException("Not implemented")
        }
    }

    @ParameterizedTest
    @MethodSource("desktopBrowserConfigAndLangSource")
    fun voidDesktopTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)
        Thread.sleep(5000)
    }

    @ParameterizedTest
    @MethodSource("mobileBrowserConfigAndLangSource")
    fun voidMobileTest(browserConfig: String, lang: String) {
        setupSite(browserConfig, lang)
        Thread.sleep(5000)
    }
}
