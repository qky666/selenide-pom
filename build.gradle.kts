import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.qky666"
version = "0.8.4"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.qky666"
            artifactId = "selenide-pom"
            version = "0.8.4"

            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
}

plugins {
    val kotlinVersion = "1.6.10"

    `java-library`
    `maven-publish`
    kotlin("jvm") version kotlinVersion
    id("io.freefair.lombok") version "6.3.0"
    id("com.github.ben-manes.versions") version "0.41.0"
}

dependencies {
    val kotlinVersion = "1.6.10"
    val jUnitVersion = "5.8.2"

    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    // https://mvnrepository.com/artifact/com.codeborne/selenide
    implementation("com.codeborne:selenide:6.2.1")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    testImplementation("org.slf4j:slf4j-simple:1.7.32")

    // https://mvnrepository.com/artifact/io.github.microutils/kotlin-logging
    implementation("io.github.microutils:kotlin-logging:2.1.21")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
//    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
