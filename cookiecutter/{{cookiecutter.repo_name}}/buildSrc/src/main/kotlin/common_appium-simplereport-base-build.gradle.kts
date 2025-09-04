package buildsrc.convention

plugins {
    id("buildsrc.convention.simplereport-base-build")
}

// Access the version catalog
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    implementation(libs.findBundle("commonAppiumSimplereportBaseBuildEcosystem").get())
}
