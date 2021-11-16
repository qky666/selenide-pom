package com.github.qky666.selenidepom.test.kotlin

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach

abstract class BaseIntegrationTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setUp() {
            Configuration.timeout = 5000
        }
    }

    @BeforeEach
    fun openURl() {
        Selenide.open("")
    }

    @AfterEach
    fun closeBrowser() {
        Selenide.closeWebDriver()
    }
}
