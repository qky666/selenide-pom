import {{ cookiecutter.group }}.gradle.AllureExtrasPlugin

repositories {
    mavenCentral()
}

plugins {
    id("base-build")
    id("io.qameta.allure-aggregate-report")
}

apply<AllureExtrasPlugin>()
