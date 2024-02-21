group = "{{ cookiecutter.group }}.mtptestng"
version = "0.0.1-SNAPSHOT"

plugins {
    id("common-simplereport-base-build")
    id("org.gradle.test-retry") version "{{ cookiecutter._test_retry_version }}"
}

dependencies {
    testImplementation(project(":common"))
}

tasks.test {
    useTestNG {
        val resources = project.sourceSets.test.get().resources.sourceDirectories.singleFile
        val wipProp = (project.findProperty("wip") ?: "false") as String
        val testngXml = if (wipProp.equals("true", ignoreCase = true)) "testng-wip.xml" else "testng.xml"
        val testngXmlFile = resources.listFiles { _, name -> name == testngXml }?.singleOrNull()
        suiteXmlFiles = if (testngXmlFile == null) listOf() else listOf(testngXmlFile)
        val retries = (project.findProperty("retry") ?: "0") as String

        retry {
            maxRetries.set(retries.toInt())
            // maxFailures.set(20)
            filter {
                includeAnnotationClasses.add("*Retry")
            }
        }
    }
}

allure {
    adapter {
        frameworks {
            testng
        }
    }
}