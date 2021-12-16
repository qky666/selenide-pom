package com.github.qky666.selenidepom.test.java.mtp;

import com.github.qky666.selenidepom.Config;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseMtpDesktopTest extends BaseMtpTest {
    @BeforeAll
    static void setUpDesktop() {
        System.clearProperty("chromeoptions.mobileEmulation");
        Config.INSTANCE.setPomVersion("desktop");
    }
}
