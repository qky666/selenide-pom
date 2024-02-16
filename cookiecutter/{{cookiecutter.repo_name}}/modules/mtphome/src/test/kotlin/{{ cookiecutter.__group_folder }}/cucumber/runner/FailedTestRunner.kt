package {{ cookiecutter.group }}.mtphome.cucumber.runner

import es.mtp.sedelantik.common.cucumber.runner.BaseTestRunner
import io.cucumber.testng.CucumberOptions
import org.testng.annotations.Test

@Test
@CucumberOptions(
    features = ["@build/test-results/cucumber/failed_scenarios.txt"],
    glue = ["{{ cookiecutter.group }}.mtphome.cucumber.steps"],
    plugin = ["{{ cookiecutter.group }}.common.cucumber.listener.CucumberListener", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm", "pretty", "json:build/test-results/cucumber/rerun.json", "rerun:build/test-results/cucumber/failed_scenarios.txt"],
)
class FailedTestRunner : BaseTestRunner()
