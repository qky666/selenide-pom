package com.github.qky666.selenidepom.test.kotlin

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.data.defaultDataPropertiesFileName
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

    @Test
    fun verifyTestData() {
        val testData = TestData(listOf(defaultDataPropertiesFileName))
        Assertions.assertEquals("", testData.input.getProperty("data.input.baseUrl"))
        testData.resetData(listOf(defaultDataPropertiesFileName, "data/prod.properties"))
        Assertions.assertEquals("https://mtp.es", testData.input.getProperty("data.input.baseUrl"))
    }
}
