package com.github.qky666.selenidepom.data

import mu.KotlinLogging
import java.io.IOException
import java.util.Properties

const val projectPropertiesFileName = "project.properties"

/**
 * Helps to use information stored in a given list of properties files.
 * The last files in list override information from previous files.
 *
 * @param propertiesFiles list of properties files to use
 * @constructor creates a new instance based on provided properties files
 */
class PropertiesHelper(propertiesFiles: List<String> = listOf(projectPropertiesFileName)) {
    private val logger = KotlinLogging.logger {}
    private val properties = Properties()
    init {
        for (file in propertiesFiles) {
            try {
                val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(file)
                if (inputStream != null) {
                    properties.load(inputStream)
                }
            } catch (ignored: IOException) {
                logger.error { "Cannot load properties file $file. Ignored exception: $ignored" }
            }
        }
    }

    /**
     * Returns the property value read from properties files.
     * The last files in list override information from previous files.
     *
     * @param property property name
     * @param defaultValue default value returned if property does not exist in properties files
     */
    @JvmOverloads
    fun getProperty(property: String, defaultValue: String = ""): String {
        return System.getProperty(property, properties.getProperty(property, defaultValue))
    }
}
