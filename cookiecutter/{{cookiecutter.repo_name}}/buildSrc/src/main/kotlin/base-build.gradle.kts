repositories {
    mavenCentral()
}

plugins {
    idea
    kotlin("jvm")
    id("com.github.ben-manes.versions")
    id("org.gradle.test-retry")
    id("io.qameta.allure-adapter")
    id("ru.vyarus.use-python")
}

dependencies {
    val log4jVersion = "{{ cookiecutter._log4j_version }}"
    val cucumberVersion = "{{ cookiecutter._cucumber_version }}"

    implementation("org.testng:testng:{{ cookiecutter._testng_version }}")
    implementation("io.cucumber:cucumber-java8:$cucumberVersion")
    implementation("io.cucumber:cucumber-testng:$cucumberVersion")
    // Import allure-bom to ensure correct versions of all the dependencies are used
    implementation(platform("io.qameta.allure:allure-bom:{{ cookiecutter._allure_bom_version }}"))
    implementation("io.qameta.allure:allure-cucumber7-jvm")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:{{ cookiecutter._log4j_api_kotlin_version }}")
    implementation(kotlin("test"))
}

allure {
    // https://mvnrepository.com/artifact/io.qameta.allure/allure-commandline
    version.set("{{ cookiecutter._allure_commandline_version }}")
    adapter {
        frameworks {
            cucumberJvm(6)
        }
    }
}

kotlin {
    jvmToolchain({{ cookiecutter._jvm_toolchain }})
}

python {
    minPythonVersion = "{{ cookiecutter._min_python_version }}"
    minPipVersion = "{{ cookiecutter._min_pip_version }}"
    // Run 'pip.exe install -U virtualenv' manually if virtualenv throws any error
    minVirtualenvVersion = "{{ cookiecutter._min_virtualenv_version }}"
    pip("allure-combine:{{ cookiecutter._allure_combine_version }}")
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
    doFirst {
        project.properties.forEach { (property, value) ->
            listOf("project.", "data.", "selenide.", "selenide-pom.", "allure.", "cucumber.").forEach {
                if (property.startsWith(it, true)) {
                    if (value != null) {
                        systemProperty(property, value)
                    }
                }
            }
            listOf("param.", "parameter.").forEach {
                if (property.startsWith(it, true)) {
                    val newPropString = property.replaceFirst(it, "", true)
                    if (value != null) {
                        systemProperty(newPropString, value)
                    }
                }
            }
        }
    }
}

tasks.compileTestKotlin {
    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
}

tasks.compileTestJava {
    options.compilerArgs.addAll(listOf("-encoding", "UTF-8"))
}

