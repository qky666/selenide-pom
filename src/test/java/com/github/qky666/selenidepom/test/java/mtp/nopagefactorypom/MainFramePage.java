package com.github.qky666.selenidepom.test.java.mtp.nopagefactorypom;

import com.github.qky666.selenidepom.Required;
import com.github.qky666.selenidepom.Page;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage extends Page {
    @Required public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
}
