package com.github.qky666.selenidepom.test.kotlin.mtp

import com.github.qky666.selenidepom.Config
import org.junit.jupiter.api.BeforeAll

abstract class BaseMtpDesktopTest : BaseMtpTest() {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setUpDesktop() {
            System.clearProperty("chromeoptions.mobileEmulation")
            Config.pomVersion = "desktop"
        }
    }
}
