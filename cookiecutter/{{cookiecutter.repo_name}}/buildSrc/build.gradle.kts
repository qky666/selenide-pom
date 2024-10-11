plugins {
    `kotlin-dsl`
    id("com.github.ben-manes.versions") version "{{ cookiecutter._versions_version }}"
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    mavenCentral()
}

dependencies {
    // Gradle plugins
    implementation("ru.vyarus:gradle-use-python-plugin:{{ cookiecutter._use_python_version }}")
    // Vulnerable dependency for gradle-use-python-plugin:4.0.0: org.apache.commons:commons-compress:1.24.0. Upgrade to: 1.26.0
    implementation("org.apache.commons:commons-compress:{{ cookiecutter._commons_compress_version }}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:{{ cookiecutter._kotlin_jvm_version }}")
    implementation("com.github.ben-manes:gradle-versions-plugin:{{ cookiecutter._versions_version }}")
    implementation("io.qameta.allure.gradle.allure:allure-plugin:{{ cookiecutter._allure_version }}")
    // Selenium
    implementation("com.codeborne:selenide:{{ cookiecutter._selenide_version }}")
    // Vulnerable dependency com.squareup.okio:okio-jvm:3.2.0. Upgrade to: 3.4.0
    implementation("com.squareup.okio:okio-jvm:{{ cookiecutter._okio_jvm_version }}")
}
