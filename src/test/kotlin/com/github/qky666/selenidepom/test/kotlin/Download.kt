package com.github.qky666.selenidepom.test.kotlin

import java.io.File
import java.net.URI
import java.nio.file.Files

const val TIDDLYWIKI_URL = "https://tiddlywiki.com/languages/es-ES/empty.html"
const val TIDDLYWIKI_FILENAME = "empty_es.html"

@Suppress("SameParameterValue")
private fun downloadFile(url: String, filename: String): File {
    val file = File("build/downloaded", filename)
    if (file.exists()) return file
    if (!file.parentFile.exists()) file.parentFile.mkdirs()
    URI(url).toURL().openStream().use { Files.copy(it, file.toPath()) }
    return file
}

@Synchronized
fun downloadTiddlywikiEs(): File {
    return downloadFile(TIDDLYWIKI_URL, TIDDLYWIKI_FILENAME)
}
