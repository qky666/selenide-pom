group = "{{ cookiecutter.group }}.common_web"
version = "0.0.1-SNAPSHOT"

plugins {
    id("buildsrc.convention.common_web-simplereport-base-build")
}

dependencies {
    implementation(libs.bundles.vulDepGradleUsePythonPlugin)
}
