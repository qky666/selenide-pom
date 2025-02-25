package com.github.qky666.selenidepom.data

import com.github.qky666.selenidepom.data.ResourceHelper.Companion.getResourceFile
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

const val PROJECT_PROPERTIES_FILENAME = "project.properties"

/**
 * Helps to use information stored in a given list of properties files.
 * The last files in list override information from previous files.
 *
 * @param propertiesFiles list of properties files to use
 * @constructor creates a new instance based on provided properties files
 */
class PropertiesHelper(propertiesFiles: List<String> = listOf(PROJECT_PROPERTIES_FILENAME)) {
    private val properties = Properties()

    init {
        propertiesFiles.forEach { fileName ->
            val resource = getResourceFile(fileName)
            resource?.let {
                InputStreamReader(FileInputStream(it), StandardCharsets.UTF_8).use { input -> properties.load(input) }
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
    fun getProperty(property: String, defaultValue: String? = null): String? {
        return System.getProperty(property, properties.getProperty(property, defaultValue))
    }
}
