package com.github.qky666.selenidepom.config

import com.codeborne.selenide.Browser
import com.codeborne.selenide.Config
import com.codeborne.selenide.webdriver.ChromeDriverFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.Proxy
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File

class ChromeMobileDriverFactory : ChromeDriverFactory() {

    private val logger = KotlinLogging.logger {}

    companion object {
        @JvmStatic
        private val threadLocalDeviceName = ThreadLocal.withInitial {
            DEFAULT_DEVICE_NAME
        }

        @JvmStatic
        var deviceName: String
            get() = threadLocalDeviceName.get()
            set(value) = threadLocalDeviceName.set(value)
    }

    override fun createCapabilities(
        config: Config,
        browser: Browser,
        proxy: Proxy?,
        browserDownloadsFolder: File?,
    ): ChromeOptions {
        val emulationCaps = ChromeOptions()
        emulationCaps.setExperimentalOption("mobileEmulation", mapOf("deviceName" to deviceName))
        val defaultCaps = super.createCapabilities(config, browser, proxy, browserDownloadsFolder)
        val finalCaps = defaultCaps.merge(emulationCaps)
        logger.info { "Final capabilities in ChromeMobileDriverFactory: $finalCaps" }
        return finalCaps
    }
}
