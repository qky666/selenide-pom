group = "{{ cookiecutter.group }}"
version = "0.0.1-SNAPSHOT"

plugins {
    id("buildsrc.convention.aggregatereport-base-build")
}

dependencies {
    // Vulnerable dependency for gradle-use-python-plugin:{{ cookiecutter._use_python_version }}: org.apache.commons:commons-compress:1.24.0. Upgrade to: 1.26.0
    implementation("org.apache.commons:commons-compress:{{ cookiecutter._commons_compress_version }}")
    // Vulnerable dependency for gradle-use-python-plugin:{{ cookiecutter._use_python_version }}: org.apache.commons:commons-lang3:3.14.0 . Upgrade to: 3.18.0
    implementation("org.apache.commons:commons-lang3:{{ cookiecutter._commons_lang3_version }}")
}

evaluationDependsOnChildren()
val modules get() = subprojects.filter { it.name !in arrayOf("common_web", "common_win", "common_android") }
val cleanTasks get() = subprojects.map { it.tasks.clean }
val testsTasks get() = subprojects.map { it.tasks.test }
val importAllureHistoryTasks get() = modules.map { it.tasks.importAllureHistory }
val allureReportTasks get() = modules.map { it.tasks.named("allureReport") }
val allureCombineTasks get() = modules.map { it.tasks.allureCombine }
val exportAllureHistoryTasks get() = modules.map { it.tasks.exportAllureHistory }

val cleanAll = tasks.register("cleanAll") {
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
