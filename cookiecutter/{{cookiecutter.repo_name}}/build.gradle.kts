group = "{{ cookiecutter.group }}"
version = "0.0.1-SNAPSHOT"

plugins {
    id("buildsrc.convention.aggregatereport-base-build")
}

dependencies {
    implementation(libs.bundles.vulDepGradleUsePythonPlugin)
}

evaluationDependsOnChildren()
val modules get() = subprojects.filter { it.name !in arrayOf("common_web", "common_win", "common_android") }
val cleanTasks get() = subprojects.map { it.tasks.clean }
val testsTasks get() = subprojects.map { it.tasks.test }
val importAllureHistoryTasks get() = modules.map { it.tasks.importAllureHistory }
val allureReportTasks get() = modules.map { it.tasks.named("allureReport") }
val allureCombineTasks get() = modules.map { it.tasks.allureCombine }
val exportAllureHistoryTasks get() = modules.map { it.tasks.exportAllureHistory }

tasks.register("cleanAll") {
    dependsOn(tasks.clean)
    dependsOn(*cleanTasks.toTypedArray())
}

val testAll = tasks.register("testAll") {
    dependsOn(testsTasks)
}

val importAllureHistoryAll = tasks.register("importAllureHistoryAll") {
    dependsOn(importAllureHistoryTasks)
}

val allureReportAll = tasks.register("allureReportAll") {
    dependsOn(allureReportTasks)
}

tasks.allureAggregateReport {
    mustRunAfter(testAll)
    mustRunAfter(*testsTasks.toTypedArray())
    mustRunAfter(importAllureHistoryAll)
    mustRunAfter(*importAllureHistoryTasks.toTypedArray())
    mustRunAfter(allureReportAll)
    mustRunAfter(allureReportTasks)
}

tasks.register("allureCombineAll") {
    dependsOn(allureCombineTasks)
    dependsOn(tasks.allureCombine)
}

tasks.register("exportAllureHistoryAll") {
    dependsOn(exportAllureHistoryTasks)
    dependsOn(tasks.exportAllureHistory)
    shouldRunAfter(allureReportAll)
    shouldRunAfter(*allureReportTasks.toTypedArray())
}
