package es.qky.selenidepom;

import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import javax.annotation.CheckReturnValue;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * Object with fields that can have @Required annotation.
 */
@ParametersAreNonnullByDefault
public interface RequiredLoadable {
    /**
     * When method shouldLoadRequired is called, all fields this @Required annotation are checked if visible.
     *
     * @param timeout The timeout for waiting to elements to become visible.
     */
    @CheckReturnValue
    default void shouldLoadRequired(Duration timeout) {
        objectShouldLoadRequired(this, timeout);
    }

    @CheckReturnValue
    private static void objectShouldLoadRequired(Object object, Duration timeout) {
        Set<String> processedNames = new HashSet<>();

        Class<?> currentClass = object.getClass();
        while (currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (processedNames.contains(fieldName)) {
                    continue;
                } else {
                    processedNames.add(fieldName);
                }
                if (field.isAnnotationPresent(Required.class)) {
                    try {
                        Object element = field.get(object);
                        if (element instanceof By byElement) {
                            $(byElement).shouldBe(visible, timeout);
                        } else if (element instanceof SelenideElement selenideElement) {
                            selenideElement.shouldBe(visible, timeout);
                        } else if (element instanceof ElementsContainer elementsContainer) {
                            // Do not want to use deprecated method getSelf
                            // elementsContainer.getSelf().shouldBe(visible, timeout);
                            objectShouldLoadRequired(elementsContainer, timeout);
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }
}
