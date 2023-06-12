package {{ cookiecutter.group }}.pom.pages.services

import com.github.qky666.selenidepom.pom.Required
import {{ cookiecutter.group }}.pom.common.MainFramePage

open class ServicesPage : MainFramePage() {
    @Required open val title = find("div.servicios-principal h1.h2")
}

val servicesPage = ServicesPage()
