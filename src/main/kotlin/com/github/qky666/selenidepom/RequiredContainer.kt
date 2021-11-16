package com.github.qky666.selenidepom

import com.codeborne.selenide.*
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDateTime
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter


/**
 * Object with properties that can have @Required annotation.
 */
interface RequiredContainer {
    /**
     * When shouldLoadRequired is called, all properties with @Required annotation are checked if visible.
     * You can override this method to add some extra functionality (custom additional checks).
     *
     * @param timeout The timeout for waiting to elements to become visible. Default value: timeout (Selenide Configuration).
     * @throws RequiredError Error can occur during validations (mostly, validation failures).
     */
    @Throws(RequiredError::class)
    fun shouldLoadRequired(timeout: Duration = Duration.ofMillis(Configuration.timeout)) {
        val errors = objectShouldLoadRequired(this, LocalDateTime.now().plus(timeout))
        if (errors.isNotEmpty()) {
            throw RequiredError(errors)
        }
    }

    /**
     * Returns true if shouldLoadRequired(timeout) returns without throwing any WebDriverException, false in otherwise.
     * You usually will not have to override this method.
     *
     * @param timeout The timeout for waiting to elements to become visible. Default value: Duration.ZERO.
     * @return true if shouldLoadRequired(timeout) returns without throwing any WebDriverException, false in otherwise.
     */
    fun hasLoadedRequired(timeout: Duration = Duration.ZERO): Boolean {
        return objectShouldLoadRequired(this, LocalDateTime.now().plus(timeout)).isEmpty()
    }

    companion object {
        private fun objectShouldLoadRequired(obj: Any, end: LocalDateTime): List<Throwable> {
            val errors = mutableListOf<Throwable>()
            val objKlass = obj::class

            // Kotlin properties
            objKlass.memberProperties.forEach {
                it.isAccessible = true
                if (it.hasAnnotation<Required>()) {
                    val element = it.javaGetter?.invoke(obj) ?: it.javaField?.get(obj)
                    errors.addAll(elementShouldLoad(element, end))
                }
            }

            val processedFields = mutableSetOf<String>()
            val processedMethods = mutableSetOf<String>()
            var currentClass: Class<*>? = obj.javaClass
            while (currentClass != null) {
                // Java fields
                val fields = currentClass.declaredFields
                    .filter {
                        it.isAccessible = true
                        processedFields.add(it.name)
                    }.filter { it.isAnnotationPresent(Required::class.java) }
                for (field in fields) {
                    errors.addAll(elementShouldLoad(field.get(obj), end))

                }
                // Java methods
                val methods = currentClass.declaredMethods
                    .filter {
                        it.isAccessible = true
                        processedMethods.add(it.name)
                    }.filter { it.isAnnotationPresent(Required::class.java) }
                for (method in methods) {
                    errors.addAll(elementShouldLoad(method.invoke(obj), end))
                }
                currentClass = currentClass.superclass
            }
            return errors
        }

        private fun elementShouldLoad(element: Any?, end: LocalDateTime): List<Throwable> {
            val signedTimeout: Duration = Duration.between(LocalDateTime.now(), end)
            val timeout: Duration = if (signedTimeout.isNegative) Duration.ZERO else signedTimeout
            when (element) {
                null -> return listOf()
                is By -> try {
                    Selenide.element(element).shouldBe(Condition.visible, timeout)
                } catch (e: Throwable) {
                    return listOf(e)
                }
                is SelenideElement -> try {
                    element.shouldBe(Condition.visible, timeout)
                } catch (e: Throwable) {
                    return listOf(e)
                }
                is ElementsCollection -> try {
                    element.shouldBe(
                        CollectionCondition.anyMatch(
                            "At least one element is visible",
                            WebElement::isDisplayed
                        ), timeout
                    )
                } catch (e: Throwable) {
                    return listOf(e)
                }
                is ElementsContainer -> return try {
                    element.self.shouldBe(Condition.visible, timeout)
                    objectShouldLoadRequired(element, end)
                } catch (e: Throwable) {
                    listOf(e)
                }
                is RequiredContainer -> return objectShouldLoadRequired(element, end)
                is WebElement -> try {
                    WebDriverWait(Selenide.webdriver().`object`(), timeout).until(
                        ExpectedConditions.visibilityOf(element)
                    )
                } catch (e: Throwable) {
                    return listOf(e)
                }
                else -> return objectShouldLoadRequired(element, end)
            }
            return listOf()
        }
    }
}