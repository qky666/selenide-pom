package com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services

import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Selenide.element
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.MainFramePage
import java.time.Duration

class ServicesShouldLoadRequiredErrorPage : MainFramePage() {
    @Required val principal = element("div.servicios-principal")
    @Required val title = element("h1.h2")

    override fun customShouldLoadRequired(timeout: Duration, pomVersion: String) {
        super.customShouldLoadRequired(timeout, pomVersion)
        title.shouldHave(text("Aseguramiento de la calidad con error"), timeout)
    }
}

val servicesShouldLoadRequiredErrorPage = ServicesShouldLoadRequiredErrorPage()