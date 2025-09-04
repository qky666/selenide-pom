// The settings file is the entry point of every Gradle build.
// Its primary purpose is to define the subprojects.
// It is also used for some aspects of project-wide configuration, like managing plugins, dependencies, etc.
// https://docs.gradle.org/current/userguide/settings_file_basics.html

dependencyResolutionManagement {
    // Use Maven Central as the default repository (where Gradle will download dependencies) in all subprojects.
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        gradlePluginPortal() // so that external plugins can be resolved in dependencies section
        maven("https://jitpack.io")
    }
}

plugins {
    // Use the Foojay Toolchains plugin to automatically download JDKs required by subprojects.
    // https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
    id("org.gradle.toolchains.foojay-resolver-convention") version "{{ cookiecutter._foojay_resolver_convention_version }}"
}

rootProject.name = "{{ cookiecutter.name }}"

include("common_web")
include("common_android")
include("common_win")

file("modules").listFiles(File::isDirectory)?.forEach { dir ->
    val name = dir.name
    include(name)
    project(":$name").projectDir = dir
}
