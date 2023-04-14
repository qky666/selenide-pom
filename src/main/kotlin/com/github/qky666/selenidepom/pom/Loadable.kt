package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.CollectionCondition.sizeGreaterThan
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.ElementsContainer
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.config.SPConfig
import mu.KotlinLogging
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.time.LocalDateTime
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses
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
     * @param model the `model` that should be used for the validations. The internal logic of the method should be aware of this `model`
     * @param lang the `language` that should be used for the validations. The internal logic of the method should be aware of this `language`
     */
    @Throws(Throwable::class)
    fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        private fun objectShouldLoadRequired(
            obj: Any,
            end: LocalDateTime,
            model: String,
            lang: String
        ): List<Throwable> {
            val errors = mutableListOf<Throwable>()
            val objKlass = obj::class
            val objKlassName: String = objKlass.simpleName ?: "null"

            val allKlass = getAllSuperKlass(objKlass).filter {
                it.isSubclassOf(Loadable::class) or it.isSubclassOf(ElementsContainer::class)
            }
            val processedNames = mutableListOf<String>()
            for (currentKlass in allKlass) {
                // Properties (Kotlin) and Fields (Java)
                currentKlass.memberProperties.forEach {
                    try {
                        it.isAccessible = true
                        logger.debug { "Property (Kotlin)/field (Java) $it made accessible" }
                    } catch (ignored: Exception) {
                        logger.debug { "Cannot make accessible property (Kotlin)/field (Java) $it. Ignored exception: $ignored" }
                        return@forEach
                    }
                    if (!processedNames.contains(it.name) and it.hasAnnotation<Required>()) {
                        val annotations = it.findAnnotations(Required::class).filter { annotation ->
                            val validModel = annotation.model.isEmpty() or annotation.model.contains(model)
                            val validLang = annotation.lang.isEmpty() or annotation.lang.contains(lang)
                            validModel and validLang
                        }
                        if (annotations.isNotEmpty()) {
                            var element = it.javaGetter?.invoke(obj) ?: it.javaField?.get(obj)
                            if (element is AtomicReference<*>) {
                                // Using @Getter(lazy=true). Use getter method
                                element = currentKlass.members.first { member ->
                                    member.name == "get" + it.name.replaceFirstChar { first ->
                                        if (first.isLowerCase()) first.titlecase(Locale.getDefault()) else first.toString()
                                    }
                                }.call(obj)
                            }
                            errors.addAll(elementShouldLoad(element, end, model, lang, objKlassName, it.name))
                        }
                    }
                    processedNames.add(it.name)
                }

                // Methods: first, make accesible
                currentKlass.functions.forEach {
                    try {
                        it.isAccessible = true
                        logger.debug { "Method $it made accessible" }
                    } catch (ignored: Exception) {
                        logger.debug { "Cannot make accessible method $it. Ignored exception: $ignored" }
                        return@forEach
                    }
                }
                // Methods: second, validate annotated functions with more than one parameter
                currentKlass.functions.filter { it.isAccessible and it.hasAnnotation<Required>() and (it.parameters.size > 1) }
                    .forEach {
                        it.parameters.filterIndexed { index, _ -> index > 0 }
                            .forEach { param -> assert(param.isOptional) { "Method $it is annotated as Required but has a parameter $param without default value" } }
                    }
                // Methods: third, process
                currentKlass.functions.filter { it.isAccessible and (it.parameters.size == 1) }.forEach {
                    if (!processedNames.contains(it.name) and it.hasAnnotation<Required>()) {
                        val annotations = it.findAnnotations(Required::class).filter { annotation ->
                            val validModel = annotation.model.isEmpty() or annotation.model.contains(model)
                            val validLang = annotation.lang.isEmpty() or annotation.lang.contains(lang)
                            validModel and validLang
                        }
                        if (annotations.isNotEmpty()) {
                            val element = it.callBy(mapOf(it.parameters[0] to obj))
                            errors.addAll(elementShouldLoad(element, end, model, lang, objKlassName, it.name))
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
            element: Any?,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {
            val errors = when (element) {
                null -> return listOf()
                is By -> byShouldLoad(element, end, model, lang, klassName, elementName)
                is LangConditionedElement -> conditionedElementShouldLoad(
                    element,
                    end,
                    model,
                    lang,
                    klassName,
                    elementName
                )

                is SelenideElement -> selenideElementShouldLoad(element, end, model, lang, klassName, elementName)
                is WidgetsCollection<*> -> widgetsCollectionShouldLoad(
                    element,
                    end,
                    model,
                    lang,
                    klassName,
                    elementName
                )

                is ElementsCollection -> elementsCollectionShouldLoad(
                    element,
                    end,
                    model,
                    lang,
                    klassName,
                    elementName
                )

                is ElementsContainer -> elementsContainerShouldLoad(
                    element,
                    end,
                    model,
                    lang,
                    klassName,
                    elementName
                )

                is Page -> objectShouldLoadRequired(element, end, model, lang)
                is WebElement -> webElementShouldLoad(element, end, model, lang, klassName, elementName)
                else -> objectShouldLoadRequired(element, end, model, lang)
            }.toMutableList()
            if (element is Loadable) {
                val timeout = calculateTimeout(end)
                try {
                    element.customShouldLoadRequired(timeout, model, lang)
                } catch (e: Throwable) {
                    errors.add(e)
                }
            }
            return errors
        }

        private fun calculateTimeout(end: LocalDateTime): Duration {
            val signedTimeout: Duration = Duration.between(LocalDateTime.now(), end)
            // Used when timeout has been already reached, to avoid setting a ZERO timeout
            val minMillisTimeout = 100L
            return if (signedTimeout.isNegative) Duration.ofMillis(minMillisTimeout) else signedTimeout
        }

        private fun byShouldLoad(
            by: By,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                element(by).shouldBe(visible, timeout)
                logger.debug {
                    "Checked element $elementName in $klassName is visible (By): ${
                    by.toString().replace("\n", "\\n")
                    }"
                }
                objectShouldLoadRequired(by, end, model, lang)
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun conditionedElementShouldLoad(
            element: LangConditionedElement,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                element.shouldBe(visible, timeout)
                logger.debug {
                    "Checked element $elementName in $klassName is visible (ConditionedElement): ${
                    element.toString().replace("\n", "\\n")
                    }"
                }
                element.shouldMeetCondition(timeout, lang)
                objectShouldLoadRequired(element, end, model, lang)
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun selenideElementShouldLoad(
            element: SelenideElement,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                element.shouldBe(visible, timeout)
                logger.debug {
                    "Checked element $elementName in $klassName is visible (SelenideElement): ${
                    element.toString().replace("\n", "\\n")
                    }"
                }
                objectShouldLoadRequired(element, end, model, lang)
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun elementsCollectionShouldLoad(
            collection: ElementsCollection,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                val visibles = collection.filter(visible).shouldBe(sizeGreaterThan(0), timeout)
                logger.debug {
                    "Checked at least one element $elementName in $klassName is visible (ElementsCollection): ${
                    collection.toString().replace("\n", "\\n")
                    }"
                }
                val errors = mutableListOf<Throwable>()
                visibles.forEach {
                    errors.addAll(objectShouldLoadRequired(it, end, model, lang))
                }
                errors.toList()
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun widgetsCollectionShouldLoad(
            widgetsCollection: WidgetsCollection<*>,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                val visibleElements = widgetsCollection.filter(visible).shouldBe(sizeGreaterThan(0), timeout)
                logger.debug {
                    "Checked at least one element $elementName in $klassName is visible (WidgetsCollection): ${
                    widgetsCollection.toString().replace("\n", "\\n")
                    }"
                }
                val errors = mutableListOf<Throwable>()
                visibleElements.forEach {
                    errors.addAll(objectShouldLoadRequired(it, end, model, lang))
                }
                for (i in 0 until visibleElements.count()) {
                    val widget = widgetsCollection[i]
                    errors.addAll(objectShouldLoadRequired(widget, end, model, lang))
                }

                errors.toList()
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun elementsContainerShouldLoad(
            container: ElementsContainer,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                container.self.shouldBe(visible, timeout)
                logger.debug {
                    "Checked element $elementName in $klassName is visible (ElementsContainer): ${
                    container.self.toString().replace("\n", "\\n")
                    }"
                }
                objectShouldLoadRequired(container, end, model, lang)
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun webElementShouldLoad(
            element: WebElement,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                WebDriverWait(Selenide.webdriver().`object`(), timeout).until(
                    ExpectedConditions.visibilityOf(element)
                )
                logger.debug {
                    "Checked element $elementName in $klassName is visible (WebElement): ${
                    element.toString().replace("\n", "\\n")
                    }"
                }
                objectShouldLoadRequired(element, end, model, lang)
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
 * @param timeout the timeout waiting for elements to become visible. Default value: Selenide's timeout
 * @param model the `model` used to check visibility. Default value: [SPConfig.model]
 * @param lang the `language` used to check visibility. Default value: [SPConfig.lang]
 * @throws RequiredError error can occur during validations (mostly, validation failures)
 * @return `this`, so it can be chained
 */
@Throws(RequiredError::class)
@JvmOverloads
fun <T : Loadable> T.shouldLoadRequired(
    timeout: Duration = Duration.ofMillis(SPConfig.selenideConfig.timeout()),
    model: String = SPConfig.model,
    lang: String = SPConfig.lang
): T {
    val logger = KotlinLogging.logger {}
    val className = this::class.simpleName ?: "null"
    logger.debug { "Starting shouldLoadRequired in class $className" }
    val end = LocalDateTime.now().plus(timeout)
    val errors = Loadable.elementShouldLoad(this, end, model, lang, className, "class_$className")
    if (errors.isNotEmpty()) {
        throw RequiredError(errors)
    }
    return this
}

/**
 * Returns `true` if [shouldLoadRequired] returns without throwing any exception, `false` otherwise.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality (custom additional checks).
 *
 * @param timeout the timeout waiting for elements to become visible. Default value: Selenide's timeout
 * @param model the `model` used to check visibility. Default value: [SPConfig.model]
 * @param lang the `language` used to check visibility. Default value: [SPConfig.lang]
 * @return `true` if [shouldLoadRequired] returns without throwing any exception, `false` otherwise
 */
@Suppress("BooleanMethodIsAlwaysInverted")
@JvmOverloads
fun <T : Loadable> T.hasLoadedRequired(
    timeout: Duration = Duration.ofMillis(SPConfig.selenideConfig.timeout()),
    model: String = SPConfig.model,
    lang: String = SPConfig.lang
): Boolean {
    val logger = KotlinLogging.logger {}
    val className = this::class.simpleName ?: "null"
    logger.debug { "Starting hasLoadedRequired in $className" }
    val end = LocalDateTime.now().plus(timeout)
    return Loadable.elementShouldLoad(this, end, model, lang, className, "root").isEmpty()
}
