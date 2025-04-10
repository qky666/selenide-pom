package com.github.qky666.selenidepom.data

import java.io.File
import java.nio.file.Path
import kotlin.io.path.toPath

/**
 * Helps to get resource's [Path].
 */
class ResourceHelper {
    companion object {
        private val loader: ClassLoader = Thread.currentThread().contextClassLoader

        private fun getResourceUri(resource: String) = loader.getResource(resource)?.toURI()

        /**
         * Get resource's full [Path], or `null` if it does not exist.
         *
         * @param resource relative path to the resource file
         * @return [resource]'s full [Path], or `null` if it does not exist
         */
        fun getResourcePath(resource: String) = getResourceUri(resource)?.toPath()

        /**
         * Get resource's full [Path] as a [String], or `null` if it does not exist.
         *
         * @param resource relative path to the resource file
         * @return [resource]'s full [Path] as a [String], or `null` if it does not exist
         */
        fun getResourcePathString(resource: String) = getResourcePath(resource)?.toString()

        /**
         * Get resource's [File], or `null` if it does not exist.
         *
         * @param resource relative path to the resource file
         * @return [resource]'s [File], or `null` if it does not exist
         */
        fun getResourceFile(resource: String) = getResourcePath(resource)?.toFile()
    }
}
