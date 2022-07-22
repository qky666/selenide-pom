package com.github.qky666.selenidepom.test.java.mtp.pom.nopagefactorypom;

import com.github.qky666.selenidepom.annotation.Required;
import com.github.qky666.selenidepom.pom.Page;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage extends Page {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
}
