package es.qky.selenidepom;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.CollectionCondition.anyMatch;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;

import javax.annotation.CheckReturnValue;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;


/**
 * Object with fields and methods (without parameters) that can have @Required annotation.
 */
@ParametersAreNonnullByDefault
public interface RequiredContainer {
    /**
     * When shouldLoadRequired is called, all fields and methods (without parameters) with @Required annotation are checked if visible.
     * You can override this method to add some extra functionality (custom additional checks).
     *
     * @param timeout The timeout for waiting to elements to become visible.
     * @throws Throwable Error can occur during validations (mostly, validation failures).
     */
    @CheckReturnValue
    default void shouldLoadRequired(Duration timeout) throws Throwable {
        objectShouldLoadRequired(this, timeout);
    }

    /**
     * All fields and methods (without parameters) with @Required annotation are checked if visible, using default timeout (Selenide Configuration).
     * You usually override shouldLoadRequired(Duration timeout) instead of this method, unless you need to change the default timeout.
     *
     * @throws Throwable Error can occur during validations (mostly, validation failures).
     */
    @CheckReturnValue
    default void shouldLoadRequired() throws Throwable {
        shouldLoadRequired(Duration.ofMillis(Configuration.timeout));
    }

    /**
     * Returns true if shouldLoadRequired(timeout) returns without throwing any WebDriverException, false in otherwise.
     * You usually will not have to override this method.
     *
     * @param timeout The timeout for waiting to elements to become visible.
     * @return true if shouldLoadRequired(timeout) returns without throwing any WebDriverException, false in otherwise.
     */
    @CheckReturnValue
    default boolean hasLoadedRequired(Duration timeout) {
        try {
            shouldLoadRequired(timeout);
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if shouldLoadRequired(Duration.ZERO) returns without throwing any WebDriverException, false in otherwise.
     * You usually will not have to override this method.
     *
     * @return true if shouldLoadRequired(Duration.ZERO) returns without throwing any WebDriverException, false in otherwise.
     */
    @CheckReturnValue
    default boolean hasAlreadyLoadedRequired() {
        return hasLoadedRequired(Duration.ZERO);
    }

    @CheckReturnValue
    static void objectShouldLoadRequired(Object object, Duration timeout) throws Throwable {
        Set<String> processedFields = new HashSet<>();
        Set<String> processedMethods = new HashSet<>();

        Class<?> currentClass = object instanceof Class<?> ? (Class<?>) object : object.getClass();
        while (currentClass != Object.class) {
            // Fields
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (processedFields.contains(fieldName)) {
                    continue;
                } else {
                    processedFields.add(fieldName);
                }
                if (field.isAnnotationPresent(Required.class)) {
                    try {
                        Object element = field.get(object);
                        elementShouldLoad(element, timeout);
                    } catch (IllegalAccessException ignored) {
                    }
                }
            }
            // Methods
            for (Method method : currentClass.getDeclaredMethods()) {
                method.setAccessible(true);
                String methodName = method.getName();
                if (processedMethods.contains(methodName)) {
                    continue;
                } else {
                    processedMethods.add(methodName);
                }
                if (method.isAnnotationPresent(Required.class)) {
                    try {
                        Object element = method.invoke(object);
                        elementShouldLoad(element, timeout);
                    } catch (InvocationTargetException | IllegalAccessException ignored) {
                    }
                }
            }

            currentClass = currentClass.getSuperclass();
        }
    }

    @CheckReturnValue
    static void elementShouldLoad(Object element, Duration timeout) throws Throwable {
        if (element instanceof By) {
            $((By) element).shouldBe(visible, timeout);
        } else if (element instanceof SelenideElement) {
            ((SelenideElement) element).shouldBe(visible, timeout);
        } else if (element instanceof ElementsCollection) {
            ((ElementsCollection) element).shouldBe(anyMatch("At least one element is visible", WebElement::isDisplayed));
        } else if (element instanceof Widget) {
            Widget widget = (Widget) element;
            widget.getSelf().shouldBe(visible, timeout);
            widget.shouldLoadRequired(timeout);
        } else if (element instanceof ElementsContainer) {
            ((ElementsContainer) element).getSelf().shouldBe(visible, timeout);
            objectShouldLoadRequired(element, timeout);
        } else if (element instanceof RequiredContainer) {
            ((RequiredContainer) element).shouldLoadRequired(timeout);
        } else if (element instanceof WebElement) {
            WebDriverWait wait = new WebDriverWait(webdriver().object(), timeout.getSeconds());
            wait.until(ExpectedConditions.visibilityOf((WebElement) element));
        } else {
            objectShouldLoadRequired(element, timeout);
        }
    }
}