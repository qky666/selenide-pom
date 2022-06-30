package com.github.qky666.selenidepom

import com.codeborne.selenide.*
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDateTime
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass
import kotlin.reflect.full.*

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
        val className = this::class.simpleName
        logger.info { "Starting shouldLoadRequired in $className" }
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
        val className = this::class.simpleName
        logger.info { "Starting hasLoadedRequired in $className" }
        return objectShouldLoadRequired(this, LocalDateTime.now().plus(timeout), pomVersion).isEmpty()
    }

    @Suppress("unused")
    companion object {
        private fun appendSuperKlassToList(klass: KClass<*>, list: MutableList<KClass<*>>): List<KClass<*>> {
            list.addAll(klass.superclasses)
            return klass.superclasses
        }

        private fun getAllSuperKlass(klass: KClass<*>): List<KClass<*>> {
            val all = mutableListOf(klass)
            var appended = appendSuperKlassToList(klass, all)
            while (appended.isNotEmpty()) {
                val newAppended = mutableListOf<KClass<*>>()
                for (newKlass in appended) {
                    newAppended.addAll(appendSuperKlassToList(newKlass, all))
                }
                appended = newAppended
            }
            return all
        }

        private fun objectShouldLoadRequired(obj: Any, end: LocalDateTime, pomVersion: String): List<Throwable> {
            val errors = mutableListOf<Throwable>()
            val objKlass = obj::class
            val objKlassName: String = objKlass.simpleName ?: "null"

            val allKlass = getAllSuperKlass(objKlass)
            val processedNames = mutableListOf<String>()
            for (currentKlass in allKlass) {
                // Properties (Kotlin) and Fields (Java)
                currentKlass.memberProperties.forEach {
                    try {
                        it.isAccessible = true
                    } catch (ignored: Exception) {
                        logger.warn { "Cannot make accessible $it" }
                        return@forEach
                    }
                    if (!processedNames.contains(it.name) && it.hasAnnotation<Required>()) {
                        val annotationValues =
                            it.findAnnotations(Required::class).map { annotation -> annotation.value }
                        if (annotationValues.contains("") || annotationValues.contains(pomVersion)) {
                            var element = it.javaGetter?.invoke(obj) ?: it.javaField?.get(obj)
                            if (element is AtomicReference<*>) {
                                // Using @Getter(lazy=true). Use getter method
                                element = currentKlass.members.first { member ->
                                    member.name == "get" + it.name.replaceFirstChar { first ->
                                        if (first.isLowerCase()) first.titlecase(Locale.getDefault()) else first.toString()
                                    }
                                }.call(obj)
                            }
                            errors.addAll(elementShouldLoad(element, end, pomVersion, objKlassName, it.name))
                        }
                    }
                    processedNames.add(it.name)
                }

                // Methods
                currentKlass.functions.forEach {
                    try {
                        it.isAccessible = true
                    } catch (ignored: Exception) {
                        logger.warn { "Cannot make accessible $it" }
                        return@forEach
                    }
                    if (!processedNames.contains(it.name) && it.hasAnnotation<Required>()) {
                        val annotationValues =
                            it.findAnnotations(Required::class).map { annotation -> annotation.value }
                        if (annotationValues.contains("") || annotationValues.contains(pomVersion)) {
                            val element = it.call(obj)
                            errors.addAll(elementShouldLoad(element, end, pomVersion, objKlassName, it.name))
                        }
                    }
                    processedNames.add(it.name)
                }
            }

            return errors
        }

        private fun elementShouldLoad(
            element: Any?, end: LocalDateTime, pomVersion: String, klassName: String, elementName: String
        ): List<Throwable> {
            val signedTimeout: Duration = Duration.between(LocalDateTime.now(), end)
            val timeout: Duration = if (signedTimeout.isNegative) Duration.ZERO else signedTimeout
            when (element) {
                null -> return listOf()
                is By -> try {
                    Selenide.element(element).shouldBe(Condition.visible, timeout)
                    logger.info {
                        "Checked element $elementName in $klassName is visible (By): ${
                            element.toString().replace("\n", "\\n")
                        }"
                    }
                } catch (e: Throwable) {
                    return listOf(e)
                }
                is SelenideElement -> try {
                    element.shouldBe(Condition.visible, timeout)
                    logger.info {
                        "Checked element $elementName in $klassName is visible (SelenideElement): ${
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
                        "Checked at least one element $elementName in $klassName is visible (ElementsCollection): ${
                            element.toString().replace("\n", "\\n")
                        }"
                    }
                } catch (e: Throwable) {
                    return listOf(e)
                }
                is ElementsContainer -> return try {
                    element.self.shouldBe(Condition.visible, timeout)
                    logger.info {
                        "Checked element $elementName in $klassName is visible (ElementsContainer): ${
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
                        "Checked element $elementName in $klassName is visible (Widget): ${
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
                        "Checked element $elementName in $klassName is visible (WebElement): ${
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
