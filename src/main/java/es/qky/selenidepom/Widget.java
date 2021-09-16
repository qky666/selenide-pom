package es.qky.selenidepom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class Widget extends ElementsContainer implements RequiredContainer {
    private SelenideElement self;

    @SuppressWarnings("unused")
    public Widget() {
        // Add this constructor to maintain some compatibility with ElementsContainer
        super();
    }

    public Widget(SelenideElement self) {
        super();
        this.self = self;
    }

    @Nonnull
    @Override
    @CheckReturnValue
    public SelenideElement getSelf() {
        return self;
    }
}
