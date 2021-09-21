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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
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
     * @throws RequiredError Error can occur during validations (mostly, validation failures).
     */
    @CheckReturnValue
    default void shouldLoadRequired(Duration timeout) throws RequiredError {
        ArrayList<Throwable> errors = objectShouldLoadRequired(this, timeout);
        if (errors.size() > 0) {
            throw new RequiredError(errors);
        }
    }

    /**
     * All fields and methods (without parameters) with @Required annotation are checked if visible, using default timeout (Selenide Configuration).
     * You usually override shouldLoadRequired(Duration timeout) instead of this method, unless you need to change the default timeout.
     *
     * @throws RequiredError Error can occur during validations (mostly, validation failures).
     */
    @CheckReturnValue
    default void shouldLoadRequired() throws RequiredError {
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
        return objectShouldLoadRequired(this, timeout).size() == 0;
    }

    /**
     * Returns true if shouldLoadRequired(Duration.ZERO) returns without throwing any WebDriverException, false in otherwise.
     * You usually will not have to override this method.
     *
     * @return true if shouldLoadRequired(Duration.ZERO) returns without throwing any WebDriverException, false in otherwise.
     */
    @CheckReturnValue
    default boolean hasAlreadyLoadedRequired() {return hasLoadedRequired(Duration.ZERO);}

    @CheckReturnValue
    @Nonnull
    static ArrayList<Throwable> objectShouldLoadRequired(Object object, Duration timeout) {
        ArrayList<Throwable> errors = new ArrayList<>();
        Duration effectiveTimeout = Duration.from(timeout);
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
                        errors.addAll(elementShouldLoad(element, effectiveTimeout));
                        if (!effectiveTimeout.isZero() && errors.size() > 0) {
                            effectiveTimeout = Duration.ZERO;
                        }
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
                        errors.addAll(elementShouldLoad(element, effectiveTimeout));
                        if (!effectiveTimeout.isZero() && errors.size() > 0) {
                            effectiveTimeout = Duration.ZERO;
                        }
                    } catch (InvocationTargetException | IllegalAccessException ignored) {
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return errors;
    }

    @CheckReturnValue
    @Nonnull
    static ArrayList<Throwable> elementShouldLoad(Object element, Duration timeout) {
        ArrayList<Throwable> errors = new ArrayList<>();
        if (element instanceof By) {
            try {
                $((By) element).shouldBe(visible, timeout);
            } catch (Throwable e) {
                errors.add(e);
            }
        } else if (element instanceof SelenideElement) {
            try {
                ((SelenideElement) element).shouldBe(visible, timeout);
            } catch (Throwable e) {
                errors.add(e);
            }
        } else if (element instanceof ElementsCollection) {
            try {
                ((ElementsCollection) element).shouldBe(anyMatch("At least one element is visible", WebElement::isDisplayed), timeout);
            } catch (Throwable e) {
                errors.add(e);
            }
        } else if (element instanceof ElementsContainer) {
            boolean lookInside = true;
            try {
                ((ElementsContainer) element).getSelf().shouldBe(visible, timeout);
            } catch (Throwable e) {
                errors.add(e);
                lookInside = false;
            }
            if (lookInside) {
                errors.addAll(objectShouldLoadRequired(element, timeout));
            }
        } else if (element instanceof RequiredContainer) {
            errors.addAll(objectShouldLoadRequired(element, timeout));
        } else if (element instanceof WebElement) {
            WebDriverWait wait = new WebDriverWait(webdriver().object(), timeout.getSeconds());
            try {
                wait.until(ExpectedConditions.visibilityOf((WebElement) element));
            } catch (Throwable e) {
                errors.add(e);
            }
        } else {
            errors.addAll(objectShouldLoadRequired(element, timeout));
        }
        return errors;
    }
}
