package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.Selenide;
import com.github.qky666.selenidepom.SPConfig;
import org.junit.jupiter.api.AfterEach;

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

    protected void setUpBrowser(String browserConfig) {
        SPConfig spConfig = SPConfig.INSTANCE;
        if (browserConfig.equals("chromeMobile")) {
            spConfig.setupBasicMobileBrowser();
        } else {
            spConfig.setupBasicDesktopBrowser(browserConfig);
        }
        Selenide.open("");
    }

    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
    }
}
