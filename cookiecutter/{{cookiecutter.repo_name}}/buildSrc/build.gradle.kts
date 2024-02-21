plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    mavenCentral()
}

dependencies {
    // Gradle plugins
    implementation("ru.vyarus:gradle-use-python-plugin:{{ cookiecutter._use_python_version }}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:{{ cookiecutter._kotlin_jvm_version }}")
    implementation("com.github.ben-manes:gradle-versions-plugin:{{ cookiecutter._versions_version }}")
    implementation("io.qameta.allure.gradle.allure:allure-plugin:{{ cookiecutter._allure_version }}")
    // Selenium
    implementation("com.codeborne:selenide:{{ cookiecutter._selenide_version }}")
}
