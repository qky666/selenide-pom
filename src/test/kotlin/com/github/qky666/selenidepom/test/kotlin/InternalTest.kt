package com.github.qky666.selenidepom.test.kotlin

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.DEFAULT_DATA_PROPERTIES_FILENAME
import com.github.qky666.selenidepom.data.PropertiesHelper
import com.github.qky666.selenidepom.data.TestData
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
        SPConfig.quitDriver()
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
        Assertions.assertEquals("", TestData.getString("data.input.baseUrl", ""))
        Assertions.assertNull(TestData.get("data.input.baseUrl", null))
        Assertions.assertThrows(RuntimeException::class.java) { TestData.get("data.input.baseUrl") }
        Assertions.assertThrows(RuntimeException::class.java) { TestData.getString("data.input.baseUrl") }
        TestData.init("internal-prod")
        Assertions.assertEquals("https://sample.com", TestData.get("data.input.baseUrl"))
        Assertions.assertEquals("https://sample.com", TestData.getString("data.input.baseUrl"))
        Assertions.assertEquals("internal-prod", TestData.env)
        Assertions.assertEquals(
            listOf(DEFAULT_DATA_PROPERTIES_FILENAME, "data/internal-prod.properties"), TestData.propertiesFiles
        )
    }

    @Test
    fun verifyInputTestDataPriority() {
        Assertions.assertEquals("", TestData.get("data.input.testOne", ""))
        Assertions.assertNull(TestData.get("data.input.testTwo", null))
        Assertions.assertThrows(RuntimeException::class.java) { TestData.get("data.input.testThree") }
        TestData.init(
            listOf(
                DEFAULT_DATA_PROPERTIES_FILENAME, "data/low-priority.properties", "data/high-priority.properties"
            )
        )
        Assertions.assertEquals("testOne_high", TestData.get("data.input.testOne"))
        Assertions.assertEquals("testOne_high", TestData.getString("data.input.testOne"))
        Assertions.assertEquals("testTwo_low", TestData.get("data.input.testTwo"))
        Assertions.assertEquals("testTwo_low", TestData.getString("data.input.testTwo"))
        Assertions.assertEquals("testThree_high", TestData.get("data.input.testThree"))
        Assertions.assertEquals("testThree_high", TestData.getString("data.input.testThree"))
    }

    @Test
    fun verifyResetOutputTestData() {
        Assertions.assertEquals(0, TestData.output.size)
        TestData.set("output.test", "Output test value")
        Assertions.assertEquals(1, TestData.output.size)
        Assertions.assertEquals("Output test value", TestData.get("output.test"))
        Assertions.assertEquals("Output test value", TestData.getString("output.test"))
        TestData.reset()
        Assertions.assertEquals(0, TestData.output.size)
    }

    @Test
    fun nonExistingFilePropertiesHelper() {
        val helper = PropertiesHelper(listOf("non-existing-file.properties"))
        Assertions.assertEquals(null, helper.getProperty("aProperty"))
    }
}
