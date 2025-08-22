group = "{{ cookiecutter.group }}.tiddlywikicucumber"
version = "0.0.1-SNAPSHOT"

plugins {
    id("buildsrc.convention.common_web-simplereport-base-build")
}

dependencies {
    // Vulnerable dependency for gradle-use-python-plugin:{{ cookiecutter._use_python_version }}: org.apache.commons:commons-compress:1.24.0. Upgrade to: 1.26.0
    implementation("org.apache.commons:commons-compress:{{ cookiecutter._commons_compress_version }}")
    // Vulnerable dependency for gradle-use-python-plugin:{{ cookiecutter._use_python_version }}: org.apache.commons:commons-lang3:3.14.0 . Upgrade to: 3.18.0
    implementation("org.apache.commons:commons-lang3:{{ cookiecutter._commons_lang3_version }}")
    testImplementation(project(":common_web"))
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
