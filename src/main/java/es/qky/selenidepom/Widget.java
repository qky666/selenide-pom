package es.qky.selenidepom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.CheckReturnValue;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;


/**
 * ElementsContainer with a constructor that sets 'self' field.
 */
@ParametersAreNonnullByDefault
public abstract class Widget extends ElementsContainer implements RequiredContainer {
    @SuppressWarnings("unused")
    @CheckReturnValue
    public Widget() {
        // Empty constructor to maintain some compatibility with ElementsContainer
        super();
    }

    @CheckReturnValue
    public Widget(SelenideElement self) {
        super();

        // Set self
        Class<?> superclass = Widget.class.getSuperclass();
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
