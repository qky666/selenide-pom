package com.github.qky666.selenidepom.kotlin

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
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Instances of this interface can have properties that can be annotated with [Required].
 */
interface RequiredContainer {
    /**
     * All properties with [Required] annotation are checked if visible.
     * You can override this method to add some extra functionality (custom additional checks).
     *
     * @param timeout The timeout waiting for elements to become visible.
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
     * Returns true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
     * You usually will not have to override this method.
     *
     * @param timeout The timeout waiting for elements to become visible.
     * @return true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
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
            return errors
        }

        private fun elementShouldLoad(element: Any?, end: LocalDateTime): List<Throwable> {
            val signedTimeout: Duration = Duration.between(LocalDateTime.now(), end)
            val timeout: Duration = if (signedTimeout.isNegative) Duration.ZERO else signedTimeout
            when (element) {
                null -> return listOf()
                is By -> try {
                    Selenide.element(element).shouldBe(Condition.visible, timeout)
                    logger.info { "Checked element is visible (By): ${element.toString().replace("\n", "\\n")}" }
                } catch (e: Throwable) {
                    return listOf(e)
                }
                is SelenideElement -> try {
                    element.shouldBe(Condition.visible, timeout)
                    logger.info {
                        "Checked element is visible (SelenideElement): ${
                            element.toString().replace("\n", "\\n")
                        }"
                    }
                } catch (e: Throwable) {
                    return listOf(e)
                }
                is ElementsCollection -> try {
                    element.shouldBe(
                        CollectionCondition.anyMatch(
                            "At least one element is visible", WebElement::isDisplayed
                        ), timeout
                    )
                    logger.info {
                        "Checked at least one element is visible (ElementsCollection): ${
                            element.toString().replace("\n", "\\n")
                        }"
                    }
                } catch (e: Throwable) {
                    return listOf(e)
                }
                is ElementsContainerWidget -> return try {
                    element.self.shouldBe(Condition.visible, timeout)
                    logger.info {
                        "Checked element is visible (ElementsContainerWidget): ${
                            element.self.toString().replace("\n", "\\n")
                        }"
                    }
                    objectShouldLoadRequired(element, end)
                } catch (e: Throwable) {
                    listOf(e)
                }
                is ElementsContainer -> return try {
                    element.self.shouldBe(Condition.visible, timeout)
                    logger.info {
                        "Checked element is visible (ElementsContainer): ${
                            element.self.toString().replace("\n", "\\n")
                        }"
                    }
                    objectShouldLoadRequired(element, end)
                } catch (e: Throwable) {
                    listOf(e)
                }
                is Widget -> return try {
                    element.self.shouldBe(Condition.visible, timeout)
                    logger.info {
                        "Checked element is visible (Widget): ${
                            element.self.toString().replace("\n", "\\n")
                        }"
                    }
                    objectShouldLoadRequired(element, end)
                } catch (e: Throwable) {
                    listOf(e)
                }
                is RequiredContainer -> return objectShouldLoadRequired(element, end)
                is WebElement -> try {
                    WebDriverWait(Selenide.webdriver().`object`(), timeout).until(
                        ExpectedConditions.visibilityOf(element)
                    )
                    logger.info { "Checked element is visible (WebElement): ${element.toString().replace("\n", "\\n")}" }
                } catch (e: Throwable) {
                    return listOf(e)
                }
                else -> return objectShouldLoadRequired(element, end)
            }
            return listOf()
        }
    }
}