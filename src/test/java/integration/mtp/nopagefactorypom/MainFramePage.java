package integration.mtp.nopagefactorypom;

import com.github.qky666.selenidepom.kotlin.Page;
import com.github.qky666.selenidepom.kotlin.Required;

import static com.codeborne.selenide.Selenide.$;

public class MainFramePage extends Page {
    @Required
    public final MainMenuWidget mainMenu = new MainMenuWidget($("div.custom-menu"));
}
