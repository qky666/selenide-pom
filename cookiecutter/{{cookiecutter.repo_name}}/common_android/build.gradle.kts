group = "{{ cookiecutter.group }}.common_android"
version = "0.0.1-SNAPSHOT"

plugins {
    id("buildsrc.convention.common_appium-simplereport-base-build")
}

dependencies {
    implementation(libs.bundles.vulDepGradleUsePythonPlugin)
}
