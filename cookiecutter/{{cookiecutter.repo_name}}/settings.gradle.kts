rootProject.name = "{{ cookiecutter.name }}"

plugins {
    // https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
    id("org.gradle.toolchains.foojay-resolver-convention") version ("{{ cookiecutter._foojay_resolver_convention_version }}")
}

include("common")

file("modules").listFiles(File::isDirectory)?.forEach { dir ->
    val name = dir.name
    include(name)
    project(":$name").projectDir = dir
}
