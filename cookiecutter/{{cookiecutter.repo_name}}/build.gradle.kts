group = "{{ cookiecutter.group }}"
version = "0.0.1-SNAPSHOT"

plugins {
    id("aggregate-report-build")
}

val modules = subprojects.filter { it.name != "common" }

task("cleanAll") {
    dependsOn(tasks.clean)
    dependsOn(subprojects.map { subproject -> subproject.tasks.matching { it.name == "clean" } })
}

task("testAll") {
    dependsOn(subprojects.map { subproject -> subproject.tasks.matching { it.name == "test" } })
}

task("importAllureHistoryAll") {
    dependsOn(modules.map { module -> module.tasks.matching { it.name == "importAllureHistory" } })
}

task("allureReportAll") {
    dependsOn(modules.map { module -> module.tasks.matching { it.name == "allureReport" } })
}

task("allureCombineAll") {
    dependsOn(modules.map { module -> module.tasks.matching { it.name == "allureCombine" } })
    dependsOn(tasks.allureCombine)
}

task("exportAllureHistoryAll") {
    dependsOn(modules.map { module -> module.tasks.matching { it.name == "exportAllureHistory" } })
}
