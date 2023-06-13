package {{ cookiecutter.group }}.pom.pages.services

import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required

class QualityAssurancePage : ServicesPage() {
    @Required override val title = LangConditionedElement(
        super.title,
        mapOf(
            "es" to "Aseguramiento de la calidad",
            "en" to "Quality assurance"
        )
    )
}

val qualityAssurancePage = QualityAssurancePage()
