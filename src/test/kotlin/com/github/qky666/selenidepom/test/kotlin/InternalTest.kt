package com.github.qky666.selenidepom.test.kotlin

import com.github.qky666.selenidepom.config.SPConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InternalTest {
    @Test
    fun verifyPomVersionFromPropertiesFile() {
        System.clearProperty("selenide-pom.pomVersion")
        SPConfig.resetSelenideConfig()
        val defaultPomVersion = SPConfig.pomVersion
        Assertions.assertEquals("filePomVersion", defaultPomVersion)
    }
}