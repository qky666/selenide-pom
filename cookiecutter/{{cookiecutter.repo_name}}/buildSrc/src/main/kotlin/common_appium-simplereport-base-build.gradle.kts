package buildsrc.convention

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

plugins {
    id("buildsrc.convention.simplereport-base-build")
}

dependencies {
    // Selenium
    implementation("com.github.qky666:selenide-pom:{{ cookiecutter._selenide_pom_version }}")
    implementation("com.codeborne:selenide-appium:{{ cookiecutter._selenide_version }}")
}
