import ru.vyarus.gradle.plugin.python.task.PythonTask

group = "{{ cookiecutter.group }}"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

plugins {
    idea
    kotlin("jvm") version "1.9.10"
    id("com.github.ben-manes.versions") version "0.49.0"
    id("io.qameta.allure") version "2.11.2"
    id("org.gradle.test-retry") version "1.5.6"
    id("ru.vyarus.use-python") version "3.0.0"
}

dependencies {
    val log4jVersion = "2.20.0"
{%- print("\n") -%}
{%- if cookiecutter.use_cucumber == "yes" -%}
{%- print("    ") -%}
    val cucumberVersion = "7.14.0"
{% endif %}
{%- print("\n    ") -%}
    testImplementation("com.github.qky666:selenide-pom:{{ cookiecutter._selenide_pom_version }}")
{%- print("\n    ") -%}
{%- if cookiecutter.use_appium == "yes" -%}
    testImplementation("com.codeborne:selenide-appium:{{ cookiecutter._selenide_version }}")
{%- print("\n") -%}
{%- else -%}
    testImplementation("com.codeborne:selenide:{{ cookiecutter._selenide_version }}")
{% endif %}
{%- if cookiecutter.use_cucumber == "yes" -%}
{%- print("    ") -%}
    testImplementation("io.cucumber:cucumber-java8:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-testng:$cucumberVersion")
    testImplementation("io.qameta.allure:allure-cucumber7-jvm:2.24.0")
{% endif %}
{%- print("    ") -%}
    testImplementation("org.testng:testng:7.8.0")
    testImplementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    testImplementation("org.apache.logging.log4j:log4j-api-kotlin:1.2.0")
    testImplementation(kotlin("test"))
}

allure {
    // https://mvnrepository.com/artifact/io.qameta.allure/allure-commandline
    version.set("2.24.1")
{%- print("\n") -%}
{%- if cookiecutter.use_cucumber == "yes" -%}
{%- print("    ") -%}
    adapter {
        frameworks {
            cucumberJvm(6)
        }
    }
{% endif %}
{%- print("") -%}
}

kotlin {
    jvmToolchain(21)
}

python {
    pip("allure-combine:1.0.11")
}

tasks.test {
    useTestNG {
        suiteXmlFiles = listOf(File("src/test/resources/testng.xml"))
{%- print("\n") -%}
{%- if cookiecutter.use_cucumber == "no" -%}
{%- print("        ") -%}
        useDefaultListeners = true
{% endif %}
{%- print("    ") -%}
    }
{%- print("\n") -%}
{%- if cookiecutter.use_cucumber == "no" -%}
{%- print("    ") -%}
    retry {
        retry {
            maxRetries.set(1)
            maxFailures.set(20)
            failOnPassedAfterRetry.set(false)
        }
    }
{% endif %}
{%- print("") -%}
}

tasks.compileTestKotlin {
    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
}

tasks.compileTestJava {
    options.compilerArgs.addAll(listOf("-encoding", "UTF-8"))
}

task<PythonTask>("allureCombine") {
    val buildDir = layout.buildDirectory.asFile.get()
    val reportDir = "$buildDir/reports/allure-report/allureReport"
    val combineDir = "$buildDir/reports/allure-combine"
    command = "-m allure_combine.combine $reportDir --dest $combineDir --auto-create-folders --remove-temp-files"
    dependsOn.add("allureReport")
}
