package com.github.qky666.selenidepom.data

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
    private val properties = Properties()

    init {
        for (file in propertiesFiles) {
            val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(file)
            if (inputStream != null) {
                properties.load(inputStream)
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
