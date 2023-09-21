group = "com.github.qky666"
version = "0.20.1"

val javaVersionNumber = 20
val javaVersion = JavaVersion.VERSION_20

plugins {
    idea
    `java-library`
    // jitpack needs maven-publish plugin
    `maven-publish`
    kotlin("jvm") version "1.9.10"
    id("io.freefair.lombok") version "8.3"
    id("com.github.ben-manes.versions") version "0.48.0"
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
    val jUnitVersion = "5.10.0"

    implementation("com.codeborne:selenide-appium:6.18.0")
    implementation(kotlin("reflect"))
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    testImplementation("org.slf4j:slf4j-simple:2.0.9")
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
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
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
