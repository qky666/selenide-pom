package com.github.qky666.selenidepom.sample

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.annotation.Required
import com.github.qky666.selenidepom.pom.Widget

@Suppress("unused")
fun newWidgetSubclassSample() {
    class MyWidget(self: SelenideElement) : Widget(self) {
        val inputField = this.find("input")
        @Required val submit = this.find("button")

        fun pressSubmit() {
            submit.click()
        }
    }
}
