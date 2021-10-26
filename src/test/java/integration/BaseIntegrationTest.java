package integration;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public abstract class BaseIntegrationTest {
    @BeforeAll
    static void setUp() {
         Configuration.timeout = 5000;
    }

    @BeforeEach
    void openURl() {
        open("");
    }

    @AfterEach
    void closeBrowser() {
        closeWebDriver();
    }
}
