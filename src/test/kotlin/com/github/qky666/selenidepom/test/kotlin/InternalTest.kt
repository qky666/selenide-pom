package com.github.qky666.selenidepom.test.kotlin

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.data.defaultDataPropertiesFileName
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InternalTest {

    @BeforeEach
    fun beforeEach() {
        TestData.init()
        SPConfig.resetConfig()
    }

    @AfterEach
    fun afterEach() {
        // Maybe not needed, but here for safety
        SPConfig.quitCurrentThreadDriver()
    }

    @Test
    fun verifyModelFromPropertiesFile() {
        System.clearProperty("selenide-pom.model")
        SPConfig.resetConfig()
        val defaultModel = SPConfig.model
        Assertions.assertEquals("fileModel", defaultModel)
    }

    @Test
    fun verifyProdInputTestData() {
        Assertions.assertEquals("", TestData.input.getProperty("data.input.baseUrl"))
        TestData.init("prod")
        Assertions.assertEquals("https://mtp.es", TestData.input.getProperty("data.input.baseUrl"))
        Assertions.assertEquals("prod", TestData.env)
        Assertions.assertEquals(listOf(defaultDataPropertiesFileName, "data/prod.properties"), TestData.propertiesFiles)
    }

    @Test
    fun verifyInputTestDataPriority() {
        Assertions.assertEquals("", TestData.input.getProperty("data.input.testOne"))
        Assertions.assertEquals("", TestData.input.getProperty("data.input.testTwo"))
        Assertions.assertEquals("", TestData.input.getProperty("data.input.testThree"))
        TestData.init(
            listOf(
                defaultDataPropertiesFileName,
                "data/low-priority.properties",
                "data/high-priority.properties"
            )
        )
        Assertions.assertEquals("testOne_high", TestData.input.getProperty("data.input.testOne"))
        Assertions.assertEquals("testTwo_low", TestData.input.getProperty("data.input.testTwo"))
        Assertions.assertEquals("testThree_high", TestData.input.getProperty("data.input.testThree"))
    }

    @Test
    fun verifyResetOutputTestData() {
        Assertions.assertEquals(0, TestData.output.size)
        TestData.output["output.test"] = "Output test value"
        Assertions.assertEquals(1, TestData.output.size)
        Assertions.assertEquals("Output test value", TestData.output["output.test"])
        TestData.resetOutputData()
        Assertions.assertEquals(0, TestData.output.size)
    }
}
