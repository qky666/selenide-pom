package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.*
import com.codeborne.selenide.CollectionCondition.sizeGreaterThan
import com.codeborne.selenide.Condition.visible
import com.github.qky666.selenidepom.annotation.Required
import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.error.RequiredError
import mu.KotlinLogging
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter

/**
 * Instances of this interface can have properties that can be annotated with [Required] annotation.
 */
interface Loadable {

    @Throws(Throwable::class)
    fun customShouldLoadRequired(timeout: Duration, pomVersion: String) {
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        fun objectShouldLoadRequired(obj: Any, end: LocalDateTime, pomVersion: String): List<Throwable> {
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

        private fun appendSuperKlassToList(klass: KClass<*>, list: MutableList<KClass<*>>): List<KClass<*>> {
            list.addAll(klass.superclasses)
            return klass.superclasses
        }

        private fun elementShouldLoad(
            element: Any?, end: LocalDateTime, pomVersion: String, klassName: String, elementName: String
        ): List<Throwable> {

            return when (element) {
                null -> return listOf()
                is By -> byShouldLoad(element, end, pomVersion, klassName, elementName)
                is SelenideElement -> selenideElementShouldLoad(element, end, pomVersion, klassName, elementName)
                is ElementsCollection -> elementsCollectionShouldLoad(element, end, pomVersion, klassName, elementName)
                is ElementsContainer -> elementsContainerShouldLoad(element, end, pomVersion, klassName, elementName)
                is Page -> objectShouldLoadRequired(element, end, pomVersion)
                is WebElement -> webElementShouldLoad(element, end, pomVersion, klassName, elementName)
                else -> objectShouldLoadRequired(element, end, pomVersion)
            }
        }

        private fun calculateTimeout(end: LocalDateTime): Duration {
            val signedTimeout: Duration = Duration.between(LocalDateTime.now(), end)
            return if (signedTimeout.isNegative) Duration.ZERO else signedTimeout
        }

        private fun byShouldLoad(
            by: By, end: LocalDateTime, pomVersion: String, klassName: String, elementName: String,
        ): List<Throwable> {

            val timeout = calculateTimeout(end)
            return try {
                Selenide.element(by).shouldBe(visible, timeout)
                logger.info {
                    "Checked element $elementName in $klassName is visible (By): ${
                        by.toString().replace("\n", "\\n")
                    }"
                }
                objectShouldLoadRequired(by, end, pomVersion)
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun selenideElementShouldLoad(
            element: SelenideElement, end: LocalDateTime, pomVersion: String, klassName: String, elementName: String
        ): List<Throwable> {

            val timeout = calculateTimeout(end)
            return try {
                element.shouldBe(visible, timeout)
                logger.info {
                    "Checked element $elementName in $klassName is visible (SelenideElement): ${
                        element.toString().replace("\n", "\\n")
                    }"
                }
                objectShouldLoadRequired(element, end, pomVersion)
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun elementsCollectionShouldLoad(
            collection: ElementsCollection,
            end: LocalDateTime,
            pomVersion: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {

            val timeout = calculateTimeout(end)
            return try {
                val visibles = collection.filter(visible).shouldBe(sizeGreaterThan(0), timeout)
                logger.info {
                    "Checked at least one element $elementName in $klassName is visible (ElementsCollection): ${
                        collection.toString().replace("\n", "\\n")
                    }"
                }
                val errors = mutableListOf<Throwable>()
                visibles.forEach {
                    errors.addAll(objectShouldLoadRequired(it, end, pomVersion))
                }
                errors.toList()
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun elementsContainerShouldLoad(
            container: ElementsContainer, end: LocalDateTime, pomVersion: String, klassName: String, elementName: String
        ): List<Throwable> {

            val timeout = calculateTimeout(end)
            return try {
                container.self.shouldBe(visible, timeout)
                logger.info {
                    "Checked element $elementName in $klassName is visible (ElementsContainer): ${
                        container.self.toString().replace("\n", "\\n")
                    }"
                }
                objectShouldLoadRequired(container, end, pomVersion)
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun webElementShouldLoad(
            element: WebElement, end: LocalDateTime, pomVersion: String, klassName: String, elementName: String
        ): List<Throwable> {

            val timeout = calculateTimeout(end)
            return try {
                WebDriverWait(Selenide.webdriver().`object`(), timeout).until(
                    ExpectedConditions.visibilityOf(element)
                )
                logger.info {
                    "Checked element $elementName in $klassName is visible (WebElement): ${
                        element.toString().replace("\n", "\\n")
                    }"
                }
                objectShouldLoadRequired(element, end, pomVersion)
            } catch (e: Throwable) {
                listOf(e)
            }
        }
    }
}

/**
 * All properties with [com.github.qky666.selenidepom.annotation.Required] annotation are checked if visible. Returns `this`.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param timeout The timeout waiting for elements to become visible. Default value: Configuration.timeout (milliseconds).
 * @param pomVersion The pomVersion used to check visibility. Default value: SPConfig.pomVersion.
 * @throws RequiredError Error can occur during validations (mostly, validation failures).
 */
@Throws(RequiredError::class)
fun <T : Loadable> T.shouldLoadRequired(timeout: Duration, pomVersion: String): T {
    val logger = KotlinLogging.logger {}
    val className = this::class.simpleName
    logger.info { "Starting shouldLoadRequired in $className" }
    val errors = Loadable.objectShouldLoadRequired(this, LocalDateTime.now().plus(timeout), pomVersion)
    if (errors.isNotEmpty()) {
        throw RequiredError(errors)
    }
    try {
        this.customShouldLoadRequired(timeout, pomVersion)
    } catch (e: Throwable) {
        throw RequiredError(listOf(e))
    }
    return this
}

/**
 * All properties with [com.github.qky666.selenidepom.annotation.Required] annotation are checked if visible. Returns `this`.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param pomVersion The pomVersion used to check visibility. Default value: SPConfig.pomVersion.
 * @throws RequiredError Error can occur during validations (mostly, validation failures).
 */
@Throws(RequiredError::class)
@JvmOverloads
fun <T : Loadable> T.shouldLoadRequired(pomVersion: String = SPConfig.pomVersion): T {
    return this.shouldLoadRequired(Duration.ofMillis(Configuration.timeout), pomVersion)
}

/**
 * All properties with [com.github.qky666.selenidepom.annotation.Required] annotation are checked if visible. Returns `this`.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param timeout The timeout waiting for elements to become visible. Default value: Configuration.timeout (milliseconds).
 * @throws RequiredError Error can occur during validations (mostly, validation failures).
 */
@Throws(RequiredError::class)
fun <T : Loadable> T.shouldLoadRequired(timeout: Duration): T {
    return this.shouldLoadRequired(timeout, SPConfig.pomVersion)
}

/**
 * Returns true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param timeout The timeout waiting for elements to become visible. Default value: Configuration.timeout (milliseconds).
 * @param pomVersion The pomVersion used to check visibility. Default value: SPConfig.pomVersion.
 * @return true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
 */
@Suppress("BooleanMethodIsAlwaysInverted")
fun <T : Loadable> T.hasLoadedRequired(timeout: Duration, pomVersion: String): Boolean {
    val logger = KotlinLogging.logger {}
    val className = this::class.simpleName
    logger.info { "Starting hasLoadedRequired in $className" }
    return Loadable.objectShouldLoadRequired(this, LocalDateTime.now().plus(timeout), pomVersion).isEmpty()
}

/**
 * Returns true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param pomVersion The pomVersion used to check visibility. Default value: SPConfig.pomVersion.
 * @return true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
 */
@Suppress("BooleanMethodIsAlwaysInverted")
@JvmOverloads
fun <T : Loadable> T.hasLoadedRequired(pomVersion: String = SPConfig.pomVersion): Boolean {
    return this.hasLoadedRequired(Duration.ofMillis(Configuration.timeout), pomVersion)
}

/**
 * Returns true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param timeout The timeout waiting for elements to become visible. Default value: Configuration.timeout (milliseconds).
 * @return true if [shouldLoadRequired] returns without throwing any exception, false otherwise.
 */
@Suppress("BooleanMethodIsAlwaysInverted")
fun <T : Loadable> T.hasLoadedRequired(timeout: Duration): Boolean {
    return this.hasLoadedRequired(timeout, SPConfig.pomVersion)
}
