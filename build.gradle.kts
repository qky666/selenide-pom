group = "com.github.qky666"
version = "0.29.0"

val javaVersionNumber = 21

plugins {
    idea
    `java-library`
    // jitpack needs maven-publish plugin
    `maven-publish`
    kotlin("jvm") version "2.0.20"
    id("io.freefair.lombok") version "8.10.2"
    id("com.github.ben-manes.versions") version "0.51.0"
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

repositories {
    mavenCentral()
}

dependencies {
    val jUnitVersion = "5.11.2"

    implementation("com.codeborne:selenide-appium:7.5.1")
    implementation(kotlin("reflect"))
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitVersion")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(javaVersionNumber)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersionNumber))
    // This is redundant, but jitpack.io seems to need it
    val javaVersion = JavaVersion.toVersion(javaVersionNumber)
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
