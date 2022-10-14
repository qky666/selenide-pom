package com.github.qky666.selenidepom.data

import mu.KotlinLogging
import java.io.IOException
import java.util.*

const val projectPropertiesFileName = "project.properties"

/**
 * Helps to use information stored in a given list of properties files.
 * The last files in list override information from previous files.
 *
 * @param propertiesFiles The list of properties files to use
 * @constructor Creates a new PropertiesHelper based on provided properties files
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
                logger.warn { "Cannot load properties file $file. Ignored exception: $ignored" }
            }
        }
    }

    /**
     * Returns the property value read from properties files.
     * The last files in list override information from previous files.
     *
     * @param property Property name
     * @param defaultValue The default value returned if property does not exist in properties files
     */
    @JvmOverloads
    fun getProperty(property: String, defaultValue: String = ""): String {
        return System.getProperty(property, properties.getProperty(property, defaultValue))
    }
}
