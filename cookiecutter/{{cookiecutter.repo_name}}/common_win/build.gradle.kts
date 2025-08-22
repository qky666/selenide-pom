group = "{{ cookiecutter.group }}.common_win"
version = "0.0.1-SNAPSHOT"

plugins {
    id("buildsrc.convention.common_appium-simplereport-base-build")
}

dependencies {
    // Vulnerable dependency for gradle-use-python-plugin:{{ cookiecutter._use_python_version }}: org.apache.commons:commons-compress:1.24.0. Upgrade to: 1.26.0
    implementation("org.apache.commons:commons-compress:{{ cookiecutter._commons_compress_version }}")
    // Vulnerable dependency for gradle-use-python-plugin:{{ cookiecutter._use_python_version }}: org.apache.commons:commons-lang3:3.14.0 . Upgrade to: 3.18.0
    implementation("org.apache.commons:commons-lang3:{{ cookiecutter._commons_lang3_version }}")
}
