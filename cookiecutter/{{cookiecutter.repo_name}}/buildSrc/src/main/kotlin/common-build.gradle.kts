plugins {
    id("simple-report-build")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}


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
