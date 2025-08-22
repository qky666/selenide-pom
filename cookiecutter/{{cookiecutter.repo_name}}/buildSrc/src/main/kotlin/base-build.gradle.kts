package buildsrc.convention

// The code in this file is a convention plugin - a Gradle mechanism for sharing reusable build logic.
// `buildSrc` is a Gradle-recognized directory and every plugin there will be easily available in the rest of the build.

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin in JVM projects.
    kotlin("jvm")
    idea
    id("com.github.ben-manes.versions")
    id("io.qameta.allure-adapter")
    id("ru.vyarus.use-python")
    id("buildsrc.convention.AllureExtras")
}

dependencies {
    val log4jVersion = "{{ cookiecutter._log4j_version }}"
    val cucumberVersion = "{{ cookiecutter._cucumber_version }}"

    implementation("org.testng:testng:{{ cookiecutter._testng_version }}")
    implementation("io.cucumber:cucumber-java8:$cucumberVersion")
    implementation("io.cucumber:cucumber-testng:$cucumberVersion")
    implementation(platform("io.qameta.allure:allure-bom:{{ cookiecutter._allure_bom_version }}"))
    implementation("io.qameta.allure:allure-cucumber7-jvm")
    implementation("io.qameta.allure:allure-testng")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:{{ cookiecutter._log4j_api_kotlin_version }}")
    implementation(kotlin("test"))
}

kotlin {
    // Use a specific Java version to make it easier to work in different environments.
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
    compilerOptions.freeCompilerArgs.add("-Xjvm-default=all")
}

tasks.compileTestJava {
    options.compilerArgs.addAll(listOf("-encoding", "UTF-8"))
}
