package com.github.qky666.selenidepom.java;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Instances of this interface can have fields or methods (without parameters) that can be annotated with
 * {@link Required}.
 */
public interface RequiredContainer {

    Logger logger = LoggerFactory.getLogger(RequiredContainer.class);

    /**
     * All fields and methods (without parameters) with {@link Required} annotation are checked if visible.
     * You can override this method to add some extra functionality (custom additional checks).
     *
     * @param timeout The timeout waiting for elements to become visible.
     * @throws RequiredError Error can occur during validations (mostly, validation failures).
     */
    default void shouldLoadRequired(Duration timeout) throws RequiredError {
        final List<Throwable> errors = objectShouldLoadRequired(this, LocalDateTime.now().plus(timeout));
        if (!errors.isEmpty()) {
            throw new RequiredError(errors);
        }
    }

    /**
     * Same as {@link #shouldLoadRequired(Duration timeout)} with
     * timeout = {@link com.codeborne.selenide.Configuration#timeout}
     * You usually override {@link #shouldLoadRequired(Duration timeout)} instead,
     * unless you need to change the default timeout.
     *
     * @throws RequiredError Error can occur during validations (mostly, validation failures).
     */
    default void shouldLoadRequired() throws RequiredError {
        shouldLoadRequired(Duration.ofMillis(Configuration.timeout));
    }

    /**
     * Returns true if {@link #shouldLoadRequired(Duration timeout)}
     * returns without throwing any exception, false otherwise.
     * You usually will not have to override this method.
     *
     * @param timeout The timeout for waiting to elements to become visible.
     * @return true if {@link #shouldLoadRequired(Duration timeout)}
     * returns without throwing any exception, false otherwise.
     */
    @CheckReturnValue
    default boolean hasLoadedRequired(Duration timeout) {
        return objectShouldLoadRequired(this, LocalDateTime.now().plus(timeout)).isEmpty();
    }

    /**
     * Returns {@link #hasLoadedRequired(Duration timeout)} with timeout = {@link Duration#ZERO}.
     * You usually will not have to override this method.
     *
     * @return {@link #hasLoadedRequired(Duration timeout)} with timeout = {@link Duration#ZERO}.
     */
    @CheckReturnValue
    default boolean hasLoadedRequired() {
        return hasLoadedRequired(Duration.ZERO);
    }

    @CheckReturnValue
    @Nonnull
    static List<Throwable> objectShouldLoadRequired(Object object, LocalDateTime end) {
        List<Throwable> errors = new ArrayList<>();
        Set<String> processedFields = Collections.synchronizedSet(new HashSet<>());
        Set<String> processedMethods = Collections.synchronizedSet(new HashSet<>());

        Class<?> currentClass = object instanceof Class<?> ? (Class<?>) object : object.getClass();
        while (currentClass != Object.class) {
            // Fields
            List<Field> fields = Arrays.stream(currentClass.getDeclaredFields()).filter(field -> {
                field.setAccessible(true);
                return processedFields.add(field.getName());
            }).filter(field -> field.isAnnotationPresent(Required.class)).collect(Collectors.toList());
            for (Field field : fields) {
                try {
                    Object element = field.get(object);
                    errors.addAll(elementShouldLoad(element, end));
                } catch (IllegalAccessException ignored) {
                }
            }

            // Methods
            List<Method> methods = Arrays.stream(currentClass.getDeclaredMethods()).filter(method -> {
                method.setAccessible(true);
                return processedMethods.add(method.getName());
            }).filter(method -> method.isAnnotationPresent(Required.class)).collect(Collectors.toList());
            for (Method method : methods) {
                try {
                    Object element = method.invoke(object);
                    errors.addAll(elementShouldLoad(element, end));
                } catch (InvocationTargetException | IllegalAccessException ignored) {
                }
            }

            currentClass = currentClass.getSuperclass();
        }
        return errors;
    }

    @CheckReturnValue
    @Nonnull
    static List<Throwable> elementShouldLoad(Object element, LocalDateTime end) {
        Duration timeout = Duration.between(LocalDateTime.now(), end);
        if (timeout.isNegative()) {
            timeout = Duration.ZERO;
        }
        if (element instanceof By) {
            try {
                Selenide.$((By) element).shouldBe(Condition.visible, timeout);
                logger.info("Checked element is visible (By): " + element.toString().replaceAll("\\n", "\\\\n"));
            } catch (Throwable e) {
                return Collections.singletonList(e);
            }
        } else if (element instanceof SelenideElement) {
            try {
                ((SelenideElement) element).shouldBe(Condition.visible, timeout);
                logger.info("Checked element is visible (SelenideElement): " + element.toString().replaceAll("\\n", "\\\\n"));
            } catch (Throwable e) {
                return Collections.singletonList(e);
            }
        } else if (element instanceof ElementsCollection) {
            try {
                ((ElementsCollection) element).shouldBe(CollectionCondition.anyMatch("At least one element is visible", WebElement::isDisplayed), timeout);
                logger.info("Checked at least one element is visible (ElementsCollection): " + element.toString().replaceAll("\\n", "\\\\n"));
            } catch (Throwable e) {
                return Collections.singletonList(e);
            }
        } else if (element instanceof ElementsContainerWidget) {
            try {
                ((ElementsContainerWidget) element).getSelf().shouldBe(Condition.visible, timeout);
                logger.info("Checked element is visible (ElementsContainerWidget): " + ((ElementsContainerWidget) element).getSelf().toString().replaceAll("\\n", "\\\\n"));
                return objectShouldLoadRequired(element, end);
            } catch (Throwable e) {
                return Collections.singletonList(e);
            }
        } else if (element instanceof ElementsContainer) {
            try {
                ((ElementsContainer) element).getSelf().shouldBe(Condition.visible, timeout);
                logger.info("Checked element is visible (ElementsContainer): " + ((ElementsContainer) element).getSelf().toString().replaceAll("\\n", "\\\\n"));
                return objectShouldLoadRequired(element, end);
            } catch (Throwable e) {
                return Collections.singletonList(e);
            }
        } else if (element instanceof Widget) {
            try {
                ((Widget) element).self.shouldBe(Condition.visible, timeout);
                logger.info("Checked element is visible (Widget): " + ((Widget) element).self.toString().replaceAll("\\n", "\\\\n"));
                return objectShouldLoadRequired(element, end);
            } catch (Throwable e) {
                return Collections.singletonList(e);
            }
        } else if (element instanceof RequiredContainer) {
            return objectShouldLoadRequired(element, end);
        } else if (element instanceof WebElement) {
            try {
                new WebDriverWait(Selenide.webdriver().object(), timeout).until(ExpectedConditions.visibilityOf((WebElement) element));
                logger.info("Checked element is visible (WebElement): " + element.toString().replaceAll("\\n", "\\\\n"));
            } catch (Throwable e) {
                return Collections.singletonList(e);
            }
        } else {
            return objectShouldLoadRequired(element, end);
        }
        return Collections.emptyList();
    }
}
