package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Widget

class BreadCrumbWidget(self: SelenideElement) : Widget(self) {
    val breadcrumbItems = findAll("li")
    val activeBreadcrumbItem = breadcrumbItems.find(Condition.cssClass("uk-active"))
}