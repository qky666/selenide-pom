package com.github.qky666.selenidepom.data

const val defaultDataPropertiesFileName = "data/default.properties"

class TestData(propertiesFiles: List<String>) {
    constructor(env: String) : this(listOf(defaultDataPropertiesFileName, "data/$env.properties"))

    private val threadLocalInput = ThreadLocal.withInitial { PropertiesHelper(propertiesFiles) }
    private val threadLocalOutput = ThreadLocal.withInitial { mutableMapOf<String, Any>() }

    val input: PropertiesHelper
        get() = threadLocalInput.get()

    val output: MutableMap<String, Any>
        get() = threadLocalOutput.get()
}
