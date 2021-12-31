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
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.functions

private val logger = KotlinLogging.logger {}

/**
 * Instances of this class can have properties that can be annotated with [Required] annotation.
 */
abstract class Page {
    /**
     * All properties with [Required] annotation are checked if visible.
     * You can override this method to add some extra functionality (custom additional checks).
     *
     * @param timeout The timeout waiting for elements to become visible.
     * Default value: com.codeborne.selenide.Configuration.timeout.
     * @param pomVersion The pomVersion used to check visibility. Default value: Config.pomVersion.
     * @throws RequiredError Error can occur during validations (mostly, validation failures).
     */
    @Throws(RequiredError::class)
    @JvmOverloads
    open fun shouldLoadRequired(
        timeout: Duration = Duration.ofMillis(Configuration.timeout), pomVersion: String = SPConfig.getPomVersion()
    ) {
        val errors = objectShouldLoadRequired(this, LocalDateTime.now().plus(timeout), pomVersion)
        if (errors.isNotEmpty()) {
            throw RequiredError(errors)
        }
    }

    /**
     * Returns true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
     *
     * @param timeout The timeout waiting for elements to become visible.
     * @param pomVersion The pomVersion used to check visibility. Default value: Config.pomVersion.
     * @return true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
     */
    @Suppress("BooleanMethodIsAlwaysInverted")
    @JvmOverloads
    fun hasLoadedRequired(timeout: Duration = Duration.ZERO, pomVersion: String = SPConfig.getPomVersion()): Boolean {
        return objectShouldLoadRequired(this, LocalDateTime.now().plus(timeout), pomVersion).isEmpty()
    }

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        private fun objectShouldLoadRequired(obj: Any, end: LocalDateTime, pomVersion: String): List<Throwable> {
            val errors = mutableListOf<Throwable>()
            val objKlass = obj::class

            // Properties (Kotlin) and Fields (Java)
            objKlass.memberProperties.forEach {
                try {
                    it.isAccessible = true
                } catch (ignored: Exception) {
                    logger.warn { "Cannot make accessible $it" }
                    return@forEach
                }
                if (it.hasAnnotation<Required>()) {
                    val annotationValues = it.findAnnotations(Required::class).map { annotation -> annotation.value }
                    if (annotationValues.contains("") || annotationValues.contains(pomVersion)) {
                        var element = it.javaGetter?.invoke(obj) ?: it.javaField?.get(obj)
                        if (element is AtomicReference<*>) {
                            // Using @Getter(lazy=true). Use getter method
                            element = objKlass.members.first { member ->
                                member.name == "get" + it.name.replaceFirstChar { first ->
                                    if (first.isLowerCase()) first.titlecase(Locale.getDefault()) else first.toString()
                                }
                            }.call(obj)
                        }
                        errors.addAll(elementShouldLoad(element, end, pomVersion))
                    }
                }
            }

            // Methods
            objKlass.functions.forEach {
                try {
                    it.isAccessible = true
                } catch (ignored: Exception) {
                    logger.warn { "Cannot make accessible $it" }
                    return@forEach
                }
                if (it.hasAnnotation<Required>()) {
                    val annotationValues = it.findAnnotations(Required::class).map { annotation -> annotation.value }
                    if (annotationValues.contains("") || annotationValues.contains(pomVersion)) {
                        val element = it.call(obj)
                        errors.addAll(elementShouldLoad(element, end, pomVersion))
                    }
                }
            }
            return errors
        }

        private fun elementShouldLoad(element: Any?, end: LocalDateTime, pomVersion: String): List<Throwable> {
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
                is ElementsContainer -> return try {
                    element.self.shouldBe(Condition.visible, timeout)
                    logger.info {
                        "Checked element is visible (ElementsContainer): ${
                            element.self.toString().replace("\n", "\\n")
                        }"
                    }
                    objectShouldLoadRequired(element, end, pomVersion)
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
                    objectShouldLoadRequired(element, end, pomVersion)
                } catch (e: Throwable) {
                    listOf(e)
                }
                is Page -> return objectShouldLoadRequired(element, end, pomVersion)
                is WebElement -> try {
                    WebDriverWait(Selenide.webdriver().`object`(), timeout).until(
                        ExpectedConditions.visibilityOf(element)
                    )
                    logger.info {
                        "Checked element is visible (WebElement): ${
                            element.toString().replace("\n", "\\n")
                        }"
                    }
                } catch (e: Throwable) {
                    return listOf(e)
                }
                else -> return objectShouldLoadRequired(element, end, pomVersion)
            }
            return listOf()
        }
    }
}
