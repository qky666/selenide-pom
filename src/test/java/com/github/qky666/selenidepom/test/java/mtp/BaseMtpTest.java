package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.Selenide;
import com.github.qky666.selenidepom.config.SPConfig;
import com.github.qky666.selenidepom.data.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

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

    TestData testData = new TestData("prod");

    protected void setUpBrowser(String browserConfig) {
        SPConfig spConfig = SPConfig.INSTANCE;
        if (browserConfig.equals("chromeMobile")) {
            spConfig.setupBasicMobileBrowser();
        } else {
            spConfig.setupBasicDesktopBrowser(browserConfig);
        }
        // testData is already initialized, but if there were more environments this could be a good place to
        // set testData
        testData.resetData("prod");
        Selenide.open(testData.getInput().getProperty("data.input.baseUrl"));
        // Additional output TestData test
        testData.getOutput().put("threadId", Thread.currentThread().getId());
    }

    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
        // Additional output TestData test
        Assertions.assertEquals(testData.getOutput().get("threadId"), Thread.currentThread().getId());
    }
}
