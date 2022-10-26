package com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services

import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Selenide.element
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.MainFramePage
import java.time.Duration

class ServicesRequiredErrorPage : MainFramePage() {
    @Required val principal = element("div.servicios-principal")
    @Required val title = element("h1.h2")
    // badSelector generates an error in shouldLoadRequired
    @Required val badSelector = element("bad-selector")
    @Required val otherBadSelector = element("other-bad-selector")

    override fun customShouldLoadRequired(timeout: Duration, pomVersion: String, lang: String) {
        super.customShouldLoadRequired(timeout, pomVersion, lang)
        title.shouldHave(text("Aseguramiento de la calidad"), timeout)
    }
}

val servicesRequiredErrorPage = ServicesRequiredErrorPage()
