package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.*
import com.codeborne.selenide.CollectionCondition.sizeGreaterThan
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide.element
import com.github.qky666.selenidepom.config.SPConfig
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
 * Instances can have properties that can be annotated with [Required] annotation.
 * All properties with this annotation are checked if visible when [shouldLoadRequired] or [hasLoadedRequired] methods
 * are executed.
 */
interface Loadable {

    /**
     * You can override this method to define custom validations performed when [shouldLoadRequired] or [hasLoadedRequired]
     * are executed. The default implementation does nothing.
     *
     * @param timeout the timeout for the operation. The internal logic of the method should be aware of this timeout
     * @param pomVersion the `pomVersion` that should be used for the validations. The internal logic of the method should be aware of this `pomVersion`
     */
    @Throws(Throwable::class)
    fun customShouldLoadRequired(timeout: Duration, pomVersion: String) {
    }

    companion object {
        private val logger = KotlinLogging.logger {}

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
                        logger.warn { "Cannot make accessible $it. Ignored exception: $ignored" }
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

        internal fun elementShouldLoad(
            element: Any?, end: LocalDateTime, pomVersion: String, klassName: String, elementName: String
        ): List<Throwable> {

            val errors = when (element) {
                null -> return listOf()
                is By -> byShouldLoad(element, end, pomVersion, klassName, elementName)
                is ConditionedElement -> textElementShouldLoad(element, end, pomVersion, klassName, elementName)
                is SelenideElement -> selenideElementShouldLoad(element, end, pomVersion, klassName, elementName)
                is WidgetsCollection<*> -> widgetsCollectionShouldLoad(element, end, pomVersion, klassName, elementName)
                is ElementsCollection -> elementsCollectionShouldLoad(element, end, pomVersion, klassName, elementName)
                is ElementsContainer -> elementsContainerShouldLoad(element, end, pomVersion, klassName, elementName)
                is Page -> objectShouldLoadRequired(element, end, pomVersion)
                is WebElement -> webElementShouldLoad(element, end, pomVersion, klassName, elementName)
                else -> objectShouldLoadRequired(element, end, pomVersion)
            }.toMutableList()
            if (element is Loadable) {
                val timeout = calculateTimeout(end)
                try {
                    element.customShouldLoadRequired(timeout, pomVersion)
                } catch (e: Throwable) {
                    errors.add(e)
                }
            }
            return errors
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
                element(by).shouldBe(visible, timeout)
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

        private fun textElementShouldLoad(
            element: ConditionedElement, end: LocalDateTime, pomVersion: String, klassName: String, elementName: String
        ): List<Throwable> {

            val timeout = calculateTimeout(end)
            return try {
                element.shouldBe(visible, timeout)
                logger.info {
                    "Checked element $elementName in $klassName is visible (TextElement): ${
                        element.toString().replace("\n", "\\n")
                    }"
                }
                element.shouldMeetCondition(timeout)
                objectShouldLoadRequired(element, end, pomVersion)
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

        private fun widgetsCollectionShouldLoad(
            widgetsCollection: WidgetsCollection<*>,
            end: LocalDateTime,
            pomVersion: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {

            val timeout = calculateTimeout(end)
            return try {
                val visibleElements = widgetsCollection.filter(visible).shouldBe(sizeGreaterThan(0), timeout)
                logger.info {
                    "Checked at least one element $elementName in $klassName is visible (WidgetsCollection): ${
                        widgetsCollection.toString().replace("\n", "\\n")
                    }"
                }
                val errors = mutableListOf<Throwable>()
                visibleElements.forEach {
                    errors.addAll(objectShouldLoadRequired(it, end, pomVersion))
                }
                for (i in 0 until visibleElements.count()) {
                    val widget = widgetsCollection[i]
                    errors.addAll(objectShouldLoadRequired(widget, end, pomVersion))
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
 * All properties with [Required] annotation are checked if visible. Returns `this`.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param timeout the timeout waiting for elements to become visible
 * @param pomVersion the `pomVersion` used to check visibility
 * @throws RequiredError error can occur during validations (mostly, validation failures)
 * @return `this`, so it can be chained
 */
@Throws(RequiredError::class)
fun <T : Loadable> T.shouldLoadRequired(timeout: Duration, pomVersion: String): T {
    val logger = KotlinLogging.logger {}
    val className = this::class.simpleName ?: "null"
    logger.info { "Starting shouldLoadRequired in class $className" }
    val end = LocalDateTime.now().plus(timeout)
    val errors = Loadable.elementShouldLoad(this, end, pomVersion, className, "class_$className")
    if (errors.isNotEmpty()) {
        throw RequiredError(errors)
    }
    return this
}

/**
 * All properties with [Required] annotation are checked if visible. Returns `this`.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param pomVersion the `pomVersion` used to check visibility. Default value: [SPConfig.pomVersion]
 * @throws RequiredError error can occur during validations (mostly, validation failures)
 * @return `this`, so it can be chained
 */
@Throws(RequiredError::class)
@JvmOverloads
fun <T : Loadable> T.shouldLoadRequired(pomVersion: String = SPConfig.pomVersion): T {
    return this.shouldLoadRequired(Duration.ofMillis(SPConfig.selenideConfig.timeout()), pomVersion)
}

/**
 * All properties with [Required] annotation are checked if visible. Returns `this`.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param timeout the timeout waiting for elements to become visible
 * @throws RequiredError error can occur during validations (mostly, validation failures).
 * @return `this`, so it can be chained
 */
@Throws(RequiredError::class)
fun <T : Loadable> T.shouldLoadRequired(timeout: Duration): T {
    return this.shouldLoadRequired(timeout, SPConfig.pomVersion)
}

/**
 * Returns `true` if [shouldLoadRequired] returns without throwing any exception, false otherwise.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param timeout the timeout waiting for elements to become visible
 * @param pomVersion `pomVersion` used to check visibility
 * @return `true` if [shouldLoadRequired] returns without throwing any exception, `false` otherwise.
 */
@Suppress("BooleanMethodIsAlwaysInverted")
fun <T : Loadable> T.hasLoadedRequired(timeout: Duration, pomVersion: String): Boolean {
    val logger = KotlinLogging.logger {}
    val className = this::class.simpleName ?: "null"
    logger.info { "Starting hasLoadedRequired in $className" }
    val end = LocalDateTime.now().plus(timeout)
    return Loadable.elementShouldLoad(this, end, pomVersion, className, "root").isEmpty()
}

/**
 * Returns `true` if [shouldLoadRequired] returns without throwing any exception, `false` otherwise.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param pomVersion `pomVersion` used to check visibility. Default value: [SPConfig.pomVersion].
 * @return `true` if [shouldLoadRequired] returns without throwing any exception, `false` otherwise.
 */
@Suppress("BooleanMethodIsAlwaysInverted")
@JvmOverloads
fun <T : Loadable> T.hasLoadedRequired(pomVersion: String = SPConfig.pomVersion): Boolean {
    return this.hasLoadedRequired(Duration.ofMillis(SPConfig.selenideConfig.timeout()), pomVersion)
}

/**
 * Returns `true` if [shouldLoadRequired] returns without throwing any exception, `false` otherwise.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param timeout the timeout waiting for elements to become visible
 * @return `true` if [shouldLoadRequired] returns without throwing any exception, `false` otherwise.
 */
@Suppress("BooleanMethodIsAlwaysInverted")
fun <T : Loadable> T.hasLoadedRequired(timeout: Duration): Boolean {
    return this.hasLoadedRequired(timeout, SPConfig.pomVersion)
}
