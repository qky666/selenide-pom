group = "{{ cookiecutter.group }}.calculatorwincucumber"
version = "0.0.1-SNAPSHOT"

plugins {
    id("buildsrc.convention.common_appium-simplereport-base-build")
}

dependencies {
    implementation(libs.bundles.vulDepGradleUsePythonPlugin)
    testImplementation(project(":common_win"))
}

tasks.test {
    useTestNG {
        val resources = project.sourceSets.test.get().resources.sourceDirectories.singleFile
        val wipProp = (project.findProperty("wip") ?: "false") as String
        val testngXml = if (wipProp.equals("true", ignoreCase = true)) "testng-wip.xml" else "testng.xml"
        val testngXmlFile = resources.listFiles { _, name -> name == testngXml }?.singleOrNull()
        val suites = if (testngXmlFile == null) listOf() else listOf(testngXmlFile)
        val retry = (project.findProperty("retry") ?: "0") as String
        val retryXmlFile = resources.listFiles { _, name -> name == "testng-retry.xml" }?.singleOrNull()
        val retrySuites = if (retryXmlFile == null) listOf() else List(retry.toInt()) { retryXmlFile }
        suiteXmlFiles = suites + retrySuites
    }
}

allure {
    adapter {
        frameworks {
            cucumberJvm(6)
        }
    }
}
