package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.Selenide;
import com.github.qky666.selenidepom.config.SPConfig;
import com.github.qky666.selenidepom.data.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BaseMtpTest {
    @SuppressWarnings("unused")
    static List<String> desktopBrowserConfigSource() {
        return Arrays.asList("chrome", "firefox");
    }

    @SuppressWarnings("unused")
    static List<String> mobileBrowserConfigSource() {
        return Collections.singletonList("chromeMobile");
    }

    SPConfig spConfig = SPConfig.INSTANCE;

    @BeforeEach
    void beforeEach() {
        TestData.INSTANCE.init("prod");
    }

    protected void setUpBrowser(String browserConfig) {
        if (browserConfig.equals("chromeMobile")) {
            spConfig.setupBasicMobileBrowser();
        } else {
            spConfig.setupBasicDesktopBrowser(browserConfig);
        }
        spConfig.setCurrentThreadDriver();
        Selenide.open(TestData.INSTANCE.getInput().getProperty("data.input.baseUrl"));
        // Additional output TestData test
        TestData.INSTANCE.getOutput().put("threadId", Thread.currentThread().getId());
    }

    @AfterEach
    void closeBrowser() {
        spConfig.quitCurrentThreadDriver();
        // Additional output TestData test
        Assertions.assertEquals(TestData.INSTANCE.getOutput().get("threadId"), Thread.currentThread().getId());
    }
}
