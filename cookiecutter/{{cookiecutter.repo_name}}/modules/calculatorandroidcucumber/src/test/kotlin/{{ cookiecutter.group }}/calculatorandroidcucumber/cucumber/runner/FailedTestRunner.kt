package {{ cookiecutter.group }}.calculatorandroidcucumber.cucumber.runner

import {{ cookiecutter.group }}.common_android.cucumber.runner.BaseTestRunner
import io.cucumber.testng.CucumberOptions
import org.testng.annotations.Test

@Test
@CucumberOptions(
    features = ["@build/test-results/cucumber/failed_scenarios.txt"],
    glue = ["{{ cookiecutter.group }}.calculatorandroidcucumber.cucumber.steps"],
    plugin = ["{{ cookiecutter.group }}.common_android.cucumber.listener.CucumberListener", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", "pretty", "json:build/test-results/cucumber/rerun.json", "rerun:build/test-results/cucumber/failed_scenarios.txt"],
    tags = "@retry",
)
class FailedTestRunner : BaseTestRunner()
