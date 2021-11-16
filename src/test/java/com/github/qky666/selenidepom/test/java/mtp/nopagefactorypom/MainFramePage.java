package com.github.qky666.selenidepom.test.java.mtp.nopagefactorypom;

import com.github.qky666.selenidepom.java.Required;
import com.github.qky666.selenidepom.java.RequiredContainer;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage implements RequiredContainer {
    @Required
    public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
}
