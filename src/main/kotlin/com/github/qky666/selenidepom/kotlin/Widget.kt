package com.github.qky666.selenidepom.kotlin

import com.codeborne.selenide.SelenideElement

abstract class Widget (open val self: SelenideElement) : RequiredContainer
