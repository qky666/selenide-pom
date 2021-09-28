package integration.mtp.nopagefactorypom;

import es.qky.selenidepom.Required;
import es.qky.selenidepom.RequiredContainer;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage implements RequiredContainer {
    @Required
    public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
}
