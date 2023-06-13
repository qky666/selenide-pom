package {{ cookiecutter.group }}.pom.common

import com.codeborne.selenide.Condition.cssClass
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.LangConditionedElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class BreadCrumbWidget(self: SelenideElement) : Widget(self) {
    val breadcrumbItems = findAll("li")

    @Required val homeItem = LangConditionedElement(breadcrumbItems.first(), "Home")

    @Required val activeBreadcrumbItem = breadcrumbItems.find(cssClass("uk-active"))
}
