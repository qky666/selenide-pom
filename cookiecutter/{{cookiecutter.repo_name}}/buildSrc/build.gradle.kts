plugins {
    `kotlin-dsl`
    alias(libs.plugins.versions)
}

dependencies {
    implementation(libs.bundles.buildSrcEcosystem)
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}
