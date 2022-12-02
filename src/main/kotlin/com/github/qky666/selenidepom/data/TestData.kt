package com.github.qky666.selenidepom.data

const val defaultDataPropertiesFileName = "data/default.properties"

/**
 * Object that helps to manage test information:
 * - Input (predefined) test information: stored in properties files, using a [PropertiesHelper].
 * - Output (generated during test run) test information: stored in a [MutableMap].
 *
 * All properties in this object are thread safe, so it can be used when tests are run in parallel.
 *
 * You should call [init] in each test before you start using it.
 */
object TestData {
    private val threadLocalPropertiesFiles = ThreadLocal.withInitial { listOf(defaultDataPropertiesFileName) }
    private val threadLocalEnv = ThreadLocal.withInitial { "" }
    private val threadLocalInput = ThreadLocal.withInitial { PropertiesHelper(propertiesFiles) }
    private val threadLocalOutput = ThreadLocal.withInitial { mutableMapOf<String, Any>() }

    /**
     * List of properties files used to get [input] test data.
     */
    val propertiesFiles: List<String>
        get() = threadLocalPropertiesFiles.get()

    /**
     * Represents the 'environment' where test is run.
     */
    val env: String
        get() = threadLocalEnv.get()

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

    /**
     * Output data is reset to an empty [MutableMap].
     */
    fun resetOutputData() {
        threadLocalOutput.set(mutableMapOf())
    }

    /**
     * Resets the object for current thread to make it usable in a test.
     *
     * @param files list of properties files to be used to get [input] test data.
     */
    fun init(files: List<String> = listOf(defaultDataPropertiesFileName)) {
        threadLocalEnv.set("")
        threadLocalPropertiesFiles.set(files)
        threadLocalInput.set(PropertiesHelper(files))
        resetOutputData()
    }

    /**
     * Resets the object for current thread to make it usable in a test.
     *
     * When an `env` is provided, [propertiesFiles] is set to a list with two elements:
     * [defaultDataPropertiesFileName] and `data/$env.properties`.
     *
     * For example: if `env` is `prod`, [propertiesFiles] is set to a list of files containing
     * [defaultDataPropertiesFileName] and `data/prod.properties`.
     * If `env` is `test`, [propertiesFiles] is set to a list of files containing
     * [defaultDataPropertiesFileName] and `data/test.properties`.
     *
     * @param env represents the 'environment' where test is run.
     */
    fun init(env: String) {
        val files = listOf(defaultDataPropertiesFileName, "data/$env.properties")
        threadLocalEnv.set(env)
        threadLocalPropertiesFiles.set(files)
        threadLocalInput.set(PropertiesHelper(files))
        resetOutputData()
    }
}
