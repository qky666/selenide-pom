package com.github.qky666.selenidepom.test.java.tiddlywiki.pom;

import com.codeborne.selenide.SelenideElement;
import com.github.qky666.selenidepom.pom.Page;
import com.github.qky666.selenidepom.pom.Required;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;

import static com.codeborne.selenide.Selenide.$;
import static com.github.qky666.selenidepom.test.kotlin.DownloadKt.downloadTiddlywikiEs;

public class MainPage extends Page {

    private static String url;

    static {
        try {
            url = downloadTiddlywikiEs().toURI().toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @NotNull
    public String getUrl() {
        return url;
    }

    @Getter(lazy = true) private final SelenideElement hideSidebar = $("button.tc-hide-sidebar-btn");
    @Getter(lazy = true) private final SelenideElement showSidebar = $("button.tc-show-sidebar-btn");
    @Getter(lazy = true, onMethod_ = {@Required})
    private final SelenideElement hideShowSidebar = $("button.tc-hide-sidebar-btn, button.tc-show-sidebar-btn");
    @Required @Getter(lazy = true)
    private final SelenideElement hideShowSidebarBis = $("button.tc-hide-sidebar-btn, button.tc-show-sidebar-btn");
}
