import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.qky666"
version = "0.5.0"

repositories {
    mavenCentral()
}

plugins {
    val kotlinVersion = "1.5.31"

    kotlin("jvm") version kotlinVersion
//    application
}

dependencies {
    val kotlinVersion = "1.5.31"

    // https://mvnrepository.com/artifact/com.codeborne/selenide
    implementation("com.codeborne:selenide:6.0.3")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    testImplementation("org.slf4j:slf4j-simple:1.7.32")

    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

//application {
//    mainClass.set("MainKt")
//}
