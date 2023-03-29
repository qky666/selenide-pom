import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val myGroup = "com.github.qky666"
val myVersion = "0.16.0"

group = myGroup
version = myVersion

val kotlinVersion = "1.8.10"

plugins {
    val kotlinVersion = "1.8.10"

    `java-library`
    `maven-publish`
    kotlin("jvm") version kotlinVersion
    id("io.freefair.lombok") version "8.0.1"
    id("com.github.ben-manes.versions") version "0.46.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
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
    val jUnitVersion = "5.9.2"

    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("com.codeborne:selenide:6.12.4")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    testImplementation("org.slf4j:slf4j-simple:2.0.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-encoding", "UTF-8"))
}
