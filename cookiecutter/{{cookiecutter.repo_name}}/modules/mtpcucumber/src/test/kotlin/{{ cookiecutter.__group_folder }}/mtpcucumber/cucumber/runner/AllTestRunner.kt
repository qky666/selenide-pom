package {{ cookiecutter.group }}.mtpcucumber.cucumber.runner

import {{ cookiecutter.group }}.common.cucumber.runner.BaseTestRunner
import io.cucumber.testng.CucumberOptions
import org.testng.annotations.Test


@Test
@CucumberOptions(
    features = ["src/test/resources/features"],
    glue = ["{{ cookiecutter.group }}.mtpcucumber.cucumber.steps"],
    plugin = ["{{ cookiecutter.group }}.common.cucumber.listener.CucumberListener", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", "pretty", "json:build/test-results/cucumber/run.json", "rerun:build/test-results/cucumber/failed_scenarios.txt"],
    tags = "(not @skip and not @wip) or @force",
)
open class AllTestRunner : BaseTestRunner()
