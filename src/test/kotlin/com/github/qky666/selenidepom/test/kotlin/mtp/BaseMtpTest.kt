package com.github.qky666.selenidepom.test.kotlin.mtp

import com.codeborne.selenide.Configuration
import com.github.qky666.selenidepom.test.kotlin.BaseIntegrationTest
import org.junit.jupiter.api.BeforeAll

abstract class BaseMtpTest : BaseIntegrationTest() {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setUpBaseUrl() {
            Configuration.baseUrl = "https://mtp.es"
        }
    }
}
