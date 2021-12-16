package com.github.qky666.selenidepom

import java.io.FileReader
import java.io.IOException
import java.util.Properties

/**
 * Main configuration values.
 * They can be set (in precedence order):
 * 1) Programmatically.
 * 2) As System property ("selenide-pom.<propertyName>").
 * 3) In selenide-pom.properties file.
 */
object Config {
    private const val fileName = "selenide-pom.properties"
    private val properties = Properties()

    /**
     * The default pomVersion value used in [Page.shouldLoadRequired] and [Page.hasLoadedRequired] methods.
     * Default value: "default".
     */
    var pomVersion: String

    init {
        try {
            properties.load(FileReader(fileName))
        } catch (ignored: IOException) {
        }
        pomVersion = System.getProperty(
            "selenide-pom.pomVersion",
            properties.getProperty("selenide-pom.pomVersion", "default")
        )
        // pomVersion = initValue("selenide-pom.pomVersion", "")
    }

//    private fun initValue(name: String, defaultValue: String): String {
//        return System.getProperty(name, properties.getProperty(name, defaultValue))
//    }
}
