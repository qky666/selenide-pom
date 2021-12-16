package com.github.qky666.selenidepom.test.kotlin.mtp

import com.github.qky666.selenidepom.Config
import org.junit.jupiter.api.BeforeAll

abstract class BaseMtpMobileTest : BaseMtpTest() {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setUpMobile() {
            System.setProperty("chromeoptions.mobileEmulation", "deviceName=Nexus 5")
            Config.pomVersion = "mobile"
        }
    }
}
