package com.github.qky666.selenidepom.sample

import com.codeborne.selenide.Condition.disabled
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.*

@Suppress("UNUSED_VARIABLE")
fun findWidgetSample() {
    class MyWidget(self: SelenideElement) : Widget(self)

    val element: MyWidget = Selenide.elements("button").findWidget(disabled, ::MyWidget )
}

@Suppress("UNUSED_VARIABLE")
fun getWidgetSample() {
    class MyWidget(self: SelenideElement) : Widget(self)

    val element: MyWidget = Selenide.elements("button").getWidget(2, ::MyWidget)
}

@Suppress("UNUSED_VARIABLE")
fun firstWidgetSample() {
    class MyWidget(self: SelenideElement) : Widget(self)

    val element: MyWidget = Selenide.elements("button").firstWidget(::MyWidget)
}

@Suppress("UNUSED_VARIABLE")
fun lastWidgetSample() {
    class MyWidget(self: SelenideElement) : Widget(self)

    val element: MyWidget = Selenide.elements("button").lastWidget(::MyWidget)
}
