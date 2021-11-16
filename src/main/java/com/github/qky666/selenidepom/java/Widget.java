package com.github.qky666.selenidepom.java;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.CheckReturnValue;

public abstract class Widget implements RequiredContainer {
    public final SelenideElement self;

    @CheckReturnValue
    public Widget(SelenideElement self) {
        this.self = self;
    }
}
