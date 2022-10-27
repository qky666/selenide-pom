package com.github.qky666.selenidepom.test.kotlin.mtp.pom.pages.services

import com.codeborne.selenide.Selenide.element
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.test.kotlin.mtp.pom.common.MainFramePage

class ServicesRequiredErrorPage : MainFramePage() {
    @Required val principal = element("div.servicios-principal")
    @Required val title = principal.find("h1.h2")
    // badSelector generates an error in shouldLoadRequired
    @Required val badSelector = element("bad-selector")
    @Required val otherBadSelector = element("other-bad-selector")
}

val servicesRequiredErrorPage = ServicesRequiredErrorPage()
