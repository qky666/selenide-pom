package {{ cookiecutter.group }}.mtpcucumber.cucumber.runner

import {{ cookiecutter.group }}.common.cucumber.runner.BaseTestRunner
import io.cucumber.testng.CucumberOptions
import org.testng.annotations.Test

@Test
@CucumberOptions(
    features = ["@build/test-results/cucumber/failed_scenarios.txt"],
    // glue = ["{{ cookiecutter.group }}.mtpcucumber.cucumber.steps"],
    glue = ["my.group.template.mtpcucumber.cucumber.steps"],
    // plugin = ["{{ cookiecutter.group }}.common.cucumber.listener.CucumberListener", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", "pretty", "json:build/test-results/cucumber/rerun.json", "rerun:build/test-results/cucumber/failed_scenarios.txt"],
    plugin = ["my.group.template.common.cucumber.listener.CucumberListener", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", "pretty", "json:build/test-results/cucumber/rerun.json", "rerun:build/test-results/cucumber/failed_scenarios.txt"],
    tags = "@retry",
)
class FailedTestRunner : BaseTestRunner()
