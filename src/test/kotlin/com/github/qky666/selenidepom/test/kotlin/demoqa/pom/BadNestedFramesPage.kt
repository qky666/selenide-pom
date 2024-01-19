package com.github.qky666.selenidepom.test.kotlin.demoqa.pom

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Iframe
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget

class BadNestedFramesPage : Page() {
    @Required val framesWrapper = BadFramesWrapper(find("div#framesWrapper"))
}

class BadFramesWrapper(self: SelenideElement) : Widget(self) {
    @Required val text = findX(".//div[contains(text(),'Sample Nested Iframe page')]")
    @Required val parentFrame = BadParentFrame(find("iframe#frame1"))
}

class BadParentFrame(self: SelenideElement) : Iframe(self) {
    @Required val body = findX(".//body[text()='Parent frame']")
    @Required val childFrame = BadChildFrame(find("iframe[srcdoc]"))
}

class BadChildFrame(self: SelenideElement) : Iframe(self) {
    @Required val text = findX(".//p[text()='Bad Child Iframe']")
}

val badNestedFramesPage = BadNestedFramesPage()
