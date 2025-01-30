package com.github.qky666.selenidepom.test.kotlin

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sikuli.script.Finder
import org.sikuli.script.Pattern
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val logger = KotlinLogging.logger {}

class OpenCVTest {

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
    fun testOpenCV() {
        val finder = Finder("C:/Users/qky66/OneDrive/Escritorio/Source.png")
        val pattern = Pattern("C:/Users/qky66/OneDrive/Escritorio/Template.png").similar(0.8)
        assertNotNull(finder.findAll(pattern))
        val matches = finder.list
        assertEquals(1, matches.size)
        logger.info { matches.first().score }
        logger.info { matches.first().center }
    }
}
