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
    id("buildsrc.convention.allure-extras")
}

// Access the version catalog
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    implementation(libs.findBundle("baseBuildEcosystem").get())
    implementation(kotlin("test"))
}

kotlin {
    // Use a specific Java version to make it easier to work in different environments.
    jvmToolchain(libs.findVersion("java").get().toString().toInt())
}

python {
    minPythonVersion = libs.findVersion("minPython").get().toString()
    minPipVersion = libs.findVersion("minPip").get().toString()
    // Run 'pip.exe install -U virtualenv' manually if virtualenv throws any error
    minVirtualenvVersion = libs.findVersion("minVirtualenv").get().toString()
    val allureCombineVersion = libs.findVersion("allureCombine").get().toString()
    pip("allure-combine:$allureCombineVersion")
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
