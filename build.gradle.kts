import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.qky666"
version = "0.12.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.qky666"
            artifactId = "selenide-pom"
            version = "0.12.0"

            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
}

plugins {
    val kotlinVersion = "1.7.10"

    `java-library`
    `maven-publish`
    kotlin("jvm") version kotlinVersion
    id("io.freefair.lombok") version "6.5.1"
    id("com.github.ben-manes.versions") version "0.42.0"
}

dependencies {
    val kotlinVersion = "1.7.10"
    val jUnitVersion = "5.9.0"

    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("com.codeborne:selenide:6.7.4")
    implementation("io.github.microutils:kotlin-logging:2.1.23")
    testImplementation("org.slf4j:slf4j-simple:2.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
//    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
//    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
