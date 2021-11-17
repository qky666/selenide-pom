import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.qky666"
version = "0.5.1"

repositories {
    mavenCentral()
}

plugins {
    val kotlinVersion = "1.5.31"

    kotlin("jvm") version kotlinVersion
    id("io.freefair.lombok") version "6.3.0"
    id("com.github.ben-manes.versions") version "0.39.0"
}

dependencies {
    val kotlinVersion = "1.5.31"

    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    // https://mvnrepository.com/artifact/com.codeborne/selenide
    implementation("com.codeborne:selenide:6.0.3")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    testImplementation("org.slf4j:slf4j-simple:1.7.32")

    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
