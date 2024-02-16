group = "{{ cookiecutter.group }}.mtphome"
version = "0.0.1-SNAPSHOT"

plugins {
    id("common-build")
    // WebService test
    // id("com.github.bjornvester.wsdl2java") version "2.0.2"
}

dependencies {
    testImplementation(project(":common"))
}

// WebService test
// wsdl2java {
//     wsdlDir.set(project.sourceSets.test.get().resources.sourceDirectories.singleFile)
// }
