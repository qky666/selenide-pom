package com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services

import com.codeborne.selenide.Condition.exactText
import com.github.qky666.selenidepom.pom.ConditionedElement
import com.github.qky666.selenidepom.pom.Required

class QualityAssurancePage : ServicesPage() {
    @Required override val title = ConditionedElement(super.title, mapOf(
        "es" to exactText("Aseguramiento de la calidad"),
        "en" to exactText("Quality assurance"),
    ))
}

val qualityAssurancePage = QualityAssurancePage()
