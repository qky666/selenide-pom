import {{ cookiecutter.group }}.gradle.AllureExtrasPlugin

repositories {
    mavenCentral()
}

plugins {
    id("base-build")
    id("io.qameta.allure-report")
}

apply<AllureExtrasPlugin>()
