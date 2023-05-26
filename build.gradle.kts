val myGroup = "com.github.qky666"
val myVersion = "0.18.4"

group = myGroup
version = myVersion

val javaVersionNumber = 17
val javaVersion = JavaVersion.VERSION_17

plugins {
    `java-library`
    // jitpack needs maven-publish plugin
    `maven-publish`
    kotlin("jvm") version "1.8.21"
    id("io.freefair.lombok") version "8.0.1"
    id("com.github.ben-manes.versions") version "0.46.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = myGroup
            artifactId = "selenide-pom"
            version = myVersion
            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val jUnitVersion = "5.9.3"

    api("com.codeborne:selenide-appium:2.8.1")
    implementation(kotlin("reflect"))
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    testImplementation("org.slf4j:slf4j-simple:2.0.7")
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("-ea", "-Xmx512m")
    systemProperties["file.encoding"] = "UTF-8"
}

tasks.compileKotlin {
    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
}

tasks.compileTestKotlin {
    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
}

tasks.compileJava {
    options.compilerArgs.addAll(listOf("-encoding", "UTF-8"))
}

tasks.compileTestJava {
    options.compilerArgs.addAll(listOf("-encoding", "UTF-8"))
}
