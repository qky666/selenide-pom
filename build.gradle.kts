group = "com.github.qky666"
version = "0.30.8"

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
    val jUnitVersion = "5.13.4"

    implementation(kotlin("reflect"))
    implementation(kotlin("test"))
    implementation("com.codeborne:selenide-appium:7.9.4")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.12")
    implementation("org.bytedeco:javacv-platform:1.5.12")
    implementation("net.sourceforge.tess4j:tess4j:5.16.0")

    testImplementation(kotlin("test"))
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnitVersion")
    testImplementation("com.google.code.findbugs:jsr305:3.0.2")
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get().toInt()))
    // This is redundant, but jitpack.io seems to need it
    val javaVersion = JavaVersion.toVersion(libs.versions.java.get().toInt())
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
