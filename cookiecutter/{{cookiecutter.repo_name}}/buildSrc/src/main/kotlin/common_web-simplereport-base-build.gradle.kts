package buildsrc.convention

plugins {
    id("buildsrc.convention.simplereport-base-build")
    // WebService test
    // id("com.github.bjornvester.wsdl2java")
}

// wsdl2java {
//     wsdlDir.set(project.sourceSets.test.get().resources.sourceDirectories.singleFile)
// }

// Access the version catalog
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    implementation(libs.findBundle("commonAppiumSimplereportBaseBuildEcosystem").get())
    // BBDD
    // implementation(libs.findBundle("bbdd").get())
    // PowerShell
    // implementation(libs.findBundle("powershell").get())
    // API
    // implementation(libs.findBundle("api").get())
}
