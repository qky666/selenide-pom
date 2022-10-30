package com.github.qky666.selenidepom.test.kotlin

import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.data.defaultDataPropertiesFileName
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InternalTest {
    @Test
    fun verifyModelFromPropertiesFile() {
        System.clearProperty("selenide-pom.model")
        SPConfig.resetSelenideConfig()
        val defaultModel = SPConfig.model
        Assertions.assertEquals("fileModel", defaultModel)
    }

    @Test
    fun verifyTestData() {
        val testData = TestData(listOf(defaultDataPropertiesFileName))
        Assertions.assertEquals("", testData.input.getProperty("data.input.baseUrl"))
        testData.resetData(listOf(defaultDataPropertiesFileName, "data/prod.properties"))
        Assertions.assertEquals("https://mtp.es", testData.input.getProperty("data.input.baseUrl"))
    }

    @AfterEach
    fun afterEach() {
        // Maybe not needed, but here for safety
        Selenide.closeWebDriver()
    }
}
