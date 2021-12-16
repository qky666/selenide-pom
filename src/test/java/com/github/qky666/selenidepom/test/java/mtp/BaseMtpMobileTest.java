package com.github.qky666.selenidepom.test.java.mtp;

import com.github.qky666.selenidepom.Config;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseMtpMobileTest extends BaseMtpTest {
    @BeforeAll
    static void setUpMobile() {
        System.setProperty("chromeoptions.mobileEmulation", "deviceName=Nexus 5");
        Config.INSTANCE.setPomVersion("mobile");
    }
}
