group = "com.github.qky666"
version = "0.30.0"

val javaVersionNumber = 21

plugins {
    idea
    `java-library`
    // jitpack needs maven-publish plugin
    `maven-publish`
    kotlin("jvm") version "2.1.10"
    id("io.freefair.lombok") version "8.12.2"
    id("com.github.ben-manes.versions") version "0.52.0"
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
    val jUnitVersion = "5.12.0"

    implementation("com.codeborne:selenide-appium:7.7.3")
    implementation(kotlin("reflect"))
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.4")
    implementation("com.sikulix:sikulixapi:2.0.5")
    // Vulnerable declared dependency (com.sikulix:sikulixapi:2.0.5)
//    implementation("org.apache.pdfbox:pdfbox:3.0.4")
//    implementation("commons-beanutils:commons-beanutils:1.10.1")
//    implementation("commons-collections:commons-collections:20040616")
//    implementation("org.apache.xmlgraphics:xmlgraphics-commons:2.10")
//    implementation("commons-io:commons-io:2.18.0")
    // Other vulnerable declared dependency
    implementation("org.apache.commons:commons-compress:1.27.1")
//    implementation("com.lowagie:itext:5.5.13.3")
//    implementation("org.bouncycastle:bcprov-jdk15on:1.78")
    implementation("org.bouncycastle:bcprov-jdk18on:1.80")
    implementation("io.netty:netty-handler:4.1.118.Final")
//    implementation("io.netty:netty-codec:4.1.68.Final")
    implementation("org.apache.ant:ant:1.10.15")
    implementation("net.sourceforge.tess4j:tess4j:5.15.0")

    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitVersion")
    testImplementation(kotlin("test"))
    implementation(kotlin("test"))
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
