package com.github.qky666.selenidepom.data

const val defaultDataPropertiesFileName = "data/default.properties"

/**
 * Class that helps to manage test information:
 * - Input (predefined) test information: stored in properties files, using a [PropertiesHelper].
 * - Output (generated during test run) test information: stored in a [MutableMap].
 *
 * This class is thread safe, so it can be used when tests are run in parallel.
 *
 * @constructor Creates a new instance using provided properties files in input data
 * @param propertiesFiles List os files used to create the [PropertiesHelper] of input data
 */
class TestData(propertiesFiles: List<String>) {
    /**
     * Creates a new instance using files: [defaultDataPropertiesFileName] and `data/$env.properties`.
     * For example: if `env` is "prod", the list of files contains [defaultDataPropertiesFileName] and `data/prod.properties`.
     * If `env` is "test", the list of files contains [defaultDataPropertiesFileName] and `data/test.properties`.
     *
     * @param env The name used in the properties file
     */
    constructor(env: String) : this(listOf(defaultDataPropertiesFileName, "data/$env.properties"))

    private val threadLocalInput = ThreadLocal.withInitial { PropertiesHelper(propertiesFiles) }
    private val threadLocalOutput = ThreadLocal.withInitial { mutableMapOf<String, Any>() }

    /**
     * The input test data. A [PropertiesHelper] instance.
     */
    val input: PropertiesHelper
        get() = threadLocalInput.get()

    /**
     * The output test data. A [MutableMap] instance.
     */
    val output: MutableMap<String, Any>
        get() = threadLocalOutput.get()
}
