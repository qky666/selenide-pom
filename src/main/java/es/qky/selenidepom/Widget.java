package es.qky.selenidepom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import lombok.NoArgsConstructor;

import javax.annotation.CheckReturnValue;
import java.lang.reflect.Field;

/**
 * ElementsContainer with a constructor that sets 'self' field.
 */
@NoArgsConstructor
public abstract class Widget extends ElementsContainer implements RequiredContainer {
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
