package com.github.qky666.selenidepom.test.java.tiddlywiki;

import com.codeborne.selenide.Selenide;
import com.github.qky666.selenidepom.config.SPConfig;
import com.github.qky666.selenidepom.data.TestData;
import com.github.qky666.selenidepom.pom.Page;
import com.github.qky666.selenidepom.test.java.tiddlywiki.pom.MainPage;
import kotlin.jvm.JvmClassMappingKt;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.github.qky666.selenidepom.pom.LoadableKt.shouldLoadRequired;
import static com.github.qky666.selenidepom.test.kotlin.DownloadKt.downloadTiddlywikiEs;

public class TiddlywikiJavaTest {

    @SuppressWarnings("unused")
    @NotNull
    public static List<Arguments> browserConfigSource() {
        return Arrays.asList(Arguments.of("chrome"), Arguments.of("firefox"), Arguments.of("chromeMobile"));
    }

    @BeforeEach
    public void beforeEach() {
        SPConfig.INSTANCE.resetConfig();
        TestData.INSTANCE.init("tiddlywiki-prod");
    }

    @AfterEach
    public void afterEach() {
        SPConfig.INSTANCE.quitDriver();
    }

    private void setupSite(@NotNull String browserConfig) {
        if (browserConfig.equalsIgnoreCase("chromeMobile")) SPConfig.INSTANCE.setupBasicMobileBrowser();
        else SPConfig.INSTANCE.setupBasicDesktopBrowser(browserConfig);
        SPConfig.INSTANCE.setDriver();
        SPConfig.INSTANCE.setLang("spa");
        Page.Companion.load(JvmClassMappingKt.getKotlinClass(MainPage.class));
    }

    @ParameterizedTest
    @MethodSource("browserConfigSource")
    public void showHideSidebarTest(@NotNull String browserConfig) {
        setupSite(browserConfig);
        MainPage mainPage = Page.Companion.getInstance(JvmClassMappingKt.getKotlinClass(MainPage.class));

        shouldLoadRequired(mainPage);
        mainPage.getHideSidebar().shouldBe(visible).click();
        mainPage.getHideSidebar().should(disappear);
        mainPage.getShowSidebar().shouldBe(visible).click();
        mainPage.getShowSidebar().should(disappear);
        mainPage.getHideSidebar().shouldBe(visible);
    }
}
