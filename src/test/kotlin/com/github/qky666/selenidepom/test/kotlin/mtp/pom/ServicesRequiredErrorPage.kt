package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide
import com.github.qky666.selenidepom.kotlin.Required
import com.github.qky666.selenidepom.kotlin.RequiredError
import java.time.Duration
import kotlin.Throws

class ServicesRequiredErrorPage : MainFramePage() {
    @Required val principal = Selenide.element("div.servicios-principal")
    @Required val title = Selenide.element("h1.h2")
    // badSelector generates an error in shouldLoadRequired
    @Required val badSelector = Selenide.element("bad-selector")
    @Required val otherBadSelector = Selenide.element("other-bad-selector")

    @Throws(RequiredError::class)
    override fun shouldLoadRequired(timeout: Duration) {
        super.shouldLoadRequired(timeout)
        title.shouldHave(Condition.text("Aseguramiento de la calidad"))
    }
}
