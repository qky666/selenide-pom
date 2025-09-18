package com.github.qky666.selenidepom.test.kotlin.demoqa.pom

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.data.TestData
import com.github.qky666.selenidepom.pom.Iframe
import com.github.qky666.selenidepom.pom.Page
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import java.net.URL

class NestedFramesPage : Page() {
    override val url = TestData.getString("data.url")!!
    @Required val framesWrapper = FramesWrapper(find("div#framesWrapper"))
}

class FramesWrapper(self: SelenideElement) : Widget(self) {
    @Required val text = findX(".//div[contains(text(),'Sample Nested Iframe page')]")

    @Required val parentFrame = ParentFrame(find("iframe#frame1"))
}

class ParentFrame(self: SelenideElement) : Iframe(self) {
    @Required val body = findX(".//body[text()='Parent frame']")
    @Required val childFrame = ChildFrame(find("iframe[srcdoc]"))
}

class ChildFrame(self: SelenideElement) : Iframe(self) {
    @Required val text = findX(".//p[text()='Child Iframe']")
}
