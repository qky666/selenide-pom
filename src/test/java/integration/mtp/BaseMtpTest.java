package integration.mtp;

import com.codeborne.selenide.Configuration;
import integration.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseMtpTest extends BaseIntegrationTest {
    @BeforeAll
    static void setUpBaseUrl() {
        Configuration.baseUrl = "https://mtp.es";
    }
}
