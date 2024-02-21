repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

plugins {
    id("simplereport-base-build")
    // WebService test
    // id("com.github.bjornvester.wsdl2java") version "<version>"
}

// wsdl2java {
//     wsdlDir.set(project.sourceSets.test.get().resources.sourceDirectories.singleFile)
// }

dependencies {
    // Selenium
    implementation("com.github.qky666:selenide-pom:{{ cookiecutter._selenide_pom_version }}")
    implementation("com.codeborne:selenide:{{ cookiecutter._selenide_version }}")
    // BBDD
    // implementation("com.microsoft.sqlserver:mssql-jdbc:<version>")
    // implementation("com.sun.xml.ws:jaxws-ri:<version>")
    // PowerShell
    // implementation("com.profesorfalken:jPowerShell:<version>")
    // API
    // val restAssuredVersion = "<version>"
    // implementation("io.rest-assured:rest-assured:$restAssuredVersion")
    // implementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    // implementation("commons-codec:commons-codec:<version>")
    // implementation("org.hamcrest:hamcrest:<version>")
}
