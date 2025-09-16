group = "com.github.qky666"
version = "0.31.0"

plugins {
    idea
    `java-library`
    // jitpack needs maven-publish plugin
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.lombok)
    alias(libs.plugins.versions)
    alias(libs.plugins.gradleJavacppPlatform)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("test"))
    implementation(libs.bundles.selenidePomEcosystem)

    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.selenidePomTestEcosystem)
}

val javaVersionNumber = libs.versions.java.get().toInt()
val javaVersion = JavaVersion.toVersion(javaVersionNumber)

kotlin {
    jvmToolchain(javaVersionNumber)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersionNumber))
    // This is redundant, but jitpack.io seems to need it
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("-ea", "-Xmx512m")
    systemProperties["file.encoding"] = "UTF-8"
}

tasks.compileKotlin {
    compilerOptions.freeCompilerArgs.add("-Xjvm-default=all")
}

tasks.compileTestKotlin {
    compilerOptions.freeCompilerArgs.add("-Xjvm-default=all")
}

tasks.compileJava {
    options.compilerArgs.addAll(listOf("-encoding", "UTF-8"))
}

tasks.compileTestJava {
    options.compilerArgs.addAll(listOf("-encoding", "UTF-8"))
}
