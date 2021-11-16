package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.Configuration;
import com.github.qky666.selenidepom.test.java.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseMtpTest extends BaseIntegrationTest {
    @BeforeAll
    static void setUpBaseUrl() {
        Configuration.baseUrl = "https://mtp.es";
    }
}
