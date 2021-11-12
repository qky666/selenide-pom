package com.github.qky666.selenidepom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import lombok.NoArgsConstructor;

import javax.annotation.CheckReturnValue;
import java.lang.reflect.Field;

/**
 * ElementsContainer with a constructor that sets 'self' field.
 */
@NoArgsConstructor
public abstract class ElementsContainerWidget extends ElementsContainer implements RequiredContainer {
    @CheckReturnValue
    public ElementsContainerWidget(SelenideElement self) {
        super();

        // Set self
        Class<?> superclass = ElementsContainerWidget.class.getSuperclass();
        Field field = null;
        try {
            field = superclass.getDeclaredField("self");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        assert field != null;
        field.setAccessible(true);
        try {
            field.set(this, self);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        assert field.isAccessible();
    }
}
