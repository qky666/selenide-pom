package com.github.qky666.selenidepom.test.java.mtp;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.WebDriverRunner;
import com.github.qky666.selenidepom.SPConfig;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;

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
        switch (browserConfig) {
            case "chrome":
                setUpChrome();
                break;
            case "firefox":
                setUpFirefox();
                break;
            case "chromeMobile":
                setUpChromeMobile();
                break;
            default:
                throw new RuntimeException("Unknown browserConfig: " + browserConfig);
        }
        SelenideConfig selenideConfig = SPConfig.INSTANCE.getSelenideConfig();
        selenideConfig.baseUrl("https://mtp.es");

        WebDriverRunner.setWebDriver(SPConfig.INSTANCE.getWebDriverFactory().createWebDriver(selenideConfig, null, null));
        Selenide.open(selenideConfig.baseUrl());
    }

    private void setUpChrome() {
        SelenideConfig selenideConfig = SPConfig.INSTANCE.getSelenideConfig();
        selenideConfig.browser("chrome");
        selenideConfig.browserCapabilities(new MutableCapabilities());
        SPConfig.INSTANCE.setPomVersion("desktop");
    }

    private void setUpFirefox() {
        SelenideConfig selenideConfig = SPConfig.INSTANCE.getSelenideConfig();
        selenideConfig.browser("firefox");
        selenideConfig.browserCapabilities(new MutableCapabilities());
        SPConfig.INSTANCE.setPomVersion("desktop");
    }

    private void setUpChromeMobile() {
        SelenideConfig selenideConfig = SPConfig.INSTANCE.getSelenideConfig();
        selenideConfig.browser("chrome");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", Collections.singletonMap("deviceName", "Nexus 5"));
        ChromeOptions newCapabilities = chromeOptions.merge(selenideConfig.browserCapabilities());
        selenideConfig.browserCapabilities(newCapabilities);
        SPConfig.INSTANCE.setPomVersion("mobile");
    }

    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
    }
}
