package com.github.qky666.selenidepom.data

import java.io.IOException
import java.util.*

const val projectPropertiesFileName = "project.properties"

class PropertiesHelper(propertiesFiles: List<String> = listOf(projectPropertiesFileName)) {
    private val properties = Properties()
    init {
        for (file in propertiesFiles) {
            try {
                val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(file)
                if (inputStream != null) {
                    properties.load(inputStream)
                }
            } catch (ignored: IOException) {
            }
        }
    }
    @JvmOverloads
    fun getProperty(property: String, defaultValue: String = ""): String {
        return System.getProperty(property, properties.getProperty(property, defaultValue))
    }
}
