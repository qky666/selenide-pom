package com.github.qky666.selenidepom.data

import com.github.qky666.selenidepom.data.TestData.init
import com.github.qky666.selenidepom.data.TestData.input

const val DEFAULT_DATA_PROPERTIES_FILENAME = "data/default.properties"

/**
 * Object that helps to manage test information:
 * - Input (predefined) test information stored in properties files, environment variables, using a [PropertiesHelper].
 * - Output (generated during test run) test information: stored in a [MutableMap].
 *
 * All properties in this object are thread safe, so it can be used when tests are run in parallel.
 *
 * You should call [init] to reset the object in each test before you start using it.
 */
object TestData {
    private val threadLocalPropertiesFiles = ThreadLocal.withInitial { listOf(DEFAULT_DATA_PROPERTIES_FILENAME) }
    private val threadLocalEnv = ThreadLocal.withInitial { "" }
    private val threadLocalInput = ThreadLocal.withInitial { PropertiesHelper(propertiesFiles) }
    private val threadLocalOutput = ThreadLocal.withInitial { mutableMapOf<String, Any>() }

    /**
     * List of properties files used to get [input] test data.
     */
    val propertiesFiles: List<String>
        get() = threadLocalPropertiesFiles.get()

    /**
     * Represents the 'environment' where test is run. Default value: empty string.
     */
    val env: String
        get() = threadLocalEnv.get()

    /**
     * The input test data. A [PropertiesHelper] instance.
     * Usually there is no need to use [input] directly:
     * [TestData.get], [TestData.getString], [TestData.set], [reset], [init] should be enough.
     */
    val input: PropertiesHelper
        get() = threadLocalInput.get()

    /**
     * The output test data. A [MutableMap] instance.
     * Usually there is no need to use [input] directly:
     * [TestData.get], [TestData.getString], [TestData.set], [reset], [init] should be enough.
     */
    val output: MutableMap<String, Any>
        get() = threadLocalOutput.get()

    /**
     * If key is empty, output data is reset to an empty [MutableMap] and `null` is returned.
     * If key is not empty, the key in output data is removed and the previous value is returned
     * (if previous value did not exist, `null` is returned).
     *
     * @param key the key to reset in output dictionary
     * @return previous `output[key]` if existed, `null` otherwise
     */
    fun reset(key: String = ""): Any? {
        if (key.isNotEmpty()) {
            return output.remove(key)
        } else {
            threadLocalOutput.set(mutableMapOf())
        }
        return null
    }

    /**
     * Returns `output[key]` if exists. If not, `input.getProperty(key)` is returned.
     * If none of them exists, default value is returned.
     * If no value is found, and no default value is provided (default = `Unit`), a RuntimeException is thrown.
     *
     * @param key the key
     * @param default the default value returned if no value is found
     * @return found value, or default value (if provided)
     * @throws RuntimeException
     */
    @Throws(RuntimeException::class)
    fun get(key: String, default: Any? = Unit): Any? {
        return output[key] ?: input.getProperty(key)
        ?: if (default == Unit) throw RuntimeException("No value found for key $key")
        else default
    }

    /**
     * Returns `output[key]` if exists. If not, `input.getProperty(key)` is returned.
     * If none of them exists, default value is returned.
     *
     * @param key the key
     * @param default the default value returned if no value is found
     * @return found value, or default value (if not found)
     */
    fun getString(key: String, default: String? = null): String? {
        return when (val value = get(key, default)) {
            is String? -> value
            else -> value.toString()
        }
    }

    /**
     * Sets `output[key]` to provided value.
     *
     * @param key the key
     * @param value the value
     */
    fun set(key: String, value: Any) {
        output[key] = value
    }

    /**
     * Resets the object for current thread to make it usable in a test.
     *
     * @param files list of properties files to be used to get [input] test data.
     */
    fun init(files: List<String> = listOf(DEFAULT_DATA_PROPERTIES_FILENAME)) {
        threadLocalEnv.set("")
        threadLocalPropertiesFiles.set(files)
        threadLocalInput.set(PropertiesHelper(files))
        reset()
    }

    /**
     * Resets the object for current thread to make it usable in a test.
     *
     * When an `env` is provided, [propertiesFiles] is set to a list with two elements:
     * [DEFAULT_DATA_PROPERTIES_FILENAME] and `data/$env.properties`.
     *
     * For example: if `env` is `prod`, [propertiesFiles] is set to a list of files containing
     * [DEFAULT_DATA_PROPERTIES_FILENAME] and `data/prod.properties`.
     * If `env` is `test`, [propertiesFiles] is set to a list of files containing
     * [DEFAULT_DATA_PROPERTIES_FILENAME] and `data/test.properties`.
     *
     * @param env represents the 'environment' where test is run.
     */
    fun init(env: String) {
        val files = listOf(DEFAULT_DATA_PROPERTIES_FILENAME, "data/$env.properties")
        threadLocalEnv.set(env)
        threadLocalPropertiesFiles.set(files)
        threadLocalInput.set(PropertiesHelper(files))
        reset()
    }
}
