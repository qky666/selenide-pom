package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.annotation.Required
import java.time.Duration

class ServicesPage : MainFramePage() {
    @Required val principal = Selenide.element("div.servicios-principal")
    @Required val title = Selenide.element("h1.h2")
    val badSelector = Selenide.element("bad-selector")

    override fun customShouldLoadRequired(timeout: Duration, pomVersion: String) {
        super.customShouldLoadRequired(timeout, pomVersion)
        title.shouldHave(Condition.text("Aseguramiento de la calidad"), timeout)
    }
}

val servicesPage = ServicesPage()
