package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.CollectionCondition.sizeGreaterThan
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Container
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.config.SPConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
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

private val logger = KotlinLogging.logger {}

/**
 * Instances can have properties that can be annotated with [Required] annotation.
 * All properties with this annotation are checked if visible when [shouldLoadRequired] or [hasLoadedRequired] methods
 * are executed.
 */
interface Loadable {

    /**
     * You can override this method to define custom validations performed when [shouldLoadRequired]
     * or [hasLoadedRequired] are executed. The default implementation does nothing.
     *
     * @param timeout the timeout for the operation. The internal logic of the method should be aware of this timeout
     * @param model the `model` that should be used for the validations. The internal logic of the method
     * should be aware of this `model`
     * @param lang the `language` that should be used for the validations. The internal logic of the method
     * should be aware of this `language`
     */
    @Throws(Throwable::class)
    fun customShouldLoadRequired(timeout: Duration, model: String, lang: String) {
    }

    companion object {

        internal fun objectShouldLoadRequired(
            obj: Any,
            end: LocalDateTime,
            model: String,
            lang: String,
        ): List<Throwable> {
            val errors = mutableListOf<Throwable>()
            val objKlass = obj::class
            val objKlassName: String = objKlass.simpleName ?: "null"

            val allKlass = getAllSuperKlass(objKlass).filter {
                val loadable = { it.isSubclassOf(Loadable::class) }
                val container = { it.isSubclassOf(Container::class) }
                loadable() || container()
            }
            val processedNames = mutableListOf<String>()
            allKlass.forEach all@{ currentKlass ->
                // Properties (Kotlin) and Fields (Java)
                currentKlass.memberProperties.forEach {
                    try {
                        it.isAccessible = true
                        logger.debug { "Property (Kotlin)/field (Java) $it made accessible" }
                    } catch (ignored: Exception) {
                        val message = "Cannot make accessible property/field $it. Ignored exception: $ignored"
                        logger.debug { message }
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
                                        if (first.isLowerCase()) first.titlecase(Locale.getDefault())
                                        else first.toString()
                                    }
                                }.call(obj)
                            }
                            val annotation = annotations.first()
                            val scroll = annotation.scroll
                            val scrollString = annotation.scrollString
                            val name = it.name
                            errors.addAll(
                                elementShouldLoad(element, end, model, lang, objKlassName, name, scroll, scrollString)
                            )
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
                currentKlass.functions.filter {
                    it.isAccessible and it.hasAnnotation<Required>() and (it.parameters.size > 1)
                }.forEach {
                    it.parameters.filterIndexed { index, _ -> index > 0 }.forEach { param ->
                        assert(param.isOptional) {
                            "Method $it is Required but has a parameter $param without default value"
                        }
                    }
                }
                // Methods: third, process
                currentKlass.functions.filter { it.isAccessible and (it.parameters.isNotEmpty()) }.forEach {
                    if (!processedNames.contains(it.name) and it.hasAnnotation<Required>()) {
                        val annotations = it.findAnnotations(Required::class).filter { annotation ->
                            val validModel = annotation.model.isEmpty() or annotation.model.contains(model)
                            val validLang = annotation.lang.isEmpty() or annotation.lang.contains(lang)
                            validModel and validLang
                        }
                        if (annotations.isNotEmpty()) {
                            val element = it.callBy(mapOf(it.parameters[0] to obj))
                            val annotation = annotations.first()
                            val scroll = annotation.scroll
                            val scrollString = annotation.scrollString
                            val name = it.name
                            errors.addAll(
                                elementShouldLoad(element, end, model, lang, objKlassName, name, scroll, scrollString)
                            )
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
                appended.forEach { newAppended.addAll(appendSuperKlassToList(it, all)) }
                appended = newAppended
            }
            return all
        }

        private fun appendSuperKlassToList(klass: KClass<*>, list: MutableList<KClass<*>>): List<KClass<*>> {
            list.addAll(klass.superclasses)
            return klass.superclasses
        }

        private fun elementShouldLoad(
            element: Any?,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String,
            scroll: Boolean,
            scrollString: String,
        ): List<Throwable> {
            val errors = when (element) {
                null -> return listOf()
                is By -> byShouldLoad(element, end, model, lang, klassName, elementName, scroll, scrollString)
                is LangConditioned -> langConditionedShouldLoad(
                    element, end, model, lang, klassName, elementName, scroll, scrollString
                )

                is SelenideElement -> selenideElementShouldLoad(
                    element, end, model, lang, klassName, elementName, scroll, scrollString
                )

                is WidgetsCollection<*> -> widgetsCollectionShouldLoad(
                    element, end, model, lang, klassName, elementName, scroll, scrollString
                )

                is ElementsCollection -> elementsCollectionShouldLoad(
                    element, end, model, lang, klassName, elementName, scroll, scrollString
                )

                is Container -> objectShouldLoadRequired(element, end, model, lang)
                is Page -> objectShouldLoadRequired(element, end, model, lang)
                is WebElement -> webElementShouldLoad(
                    element, end, model, lang, klassName, elementName, scroll, scrollString
                )

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

        internal fun calculateTimeout(end: LocalDateTime): Duration {
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
            elementName: String,
            scroll: Boolean,
            scrollString: String,
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                val element = Page.find(by)
                if (scroll) element.scrollIntoView(scrollString)
                element.shouldBe(visible, timeout)
                val elementLog = by.toString().replace("\n", "\\n")
                logger.debug { "Checked element $elementName in $klassName is visible (By): $elementLog" }
                objectShouldLoadRequired(by, end, model, lang)
            } catch (e: Throwable) {
                listOf(e)
            }
        }

        private fun langConditionedShouldLoad(
            element: LangConditioned,
            end: LocalDateTime,
            model: String,
            lang: String,
            klassName: String,
            elementName: String,
            scroll: Boolean,
            scrollString: String,
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                if (scroll) element.scrollIntoView(scrollString)
                element.shouldBe(visible, timeout)
                val elementLog = element.toString().replace("\n", "\\n")
                logger.debug { "Checked element $elementName in $klassName is visible (LangConditioned): $elementLog" }
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
            elementName: String,
            scroll: Boolean,
            scrollString: String,
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                if (scroll) element.scrollIntoView(scrollString)
                element.shouldBe(visible, timeout)
                val elementLog = element.toString().replace("\n", "\\n")
                logger.debug { "Checked element $elementName in $klassName is visible (SelenideElement): $elementLog" }
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
            elementName: String,
            scroll: Boolean,
            scrollString: String,
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                if (scroll) collection.first().scrollIntoView(scrollString)
                val filtered = collection.filter(visible).shouldBe(sizeGreaterThan(0), timeout)
                val filteredSize = filtered.size()
                val checked = "Checked at least one element $elementName in $klassName is visible (ElementsCollection)"
                val collectionLog = collection.toString().replace("\n", "\\n")
                logger.debug { "$checked: $collectionLog" }
                val errors = mutableListOf<Throwable>()
                // 'filtered' can change during the operation, so 'while' is used instead of 'forEach'
                var i = 0
                while (i < filtered.size() && i < filteredSize) {
                    val element = filtered[i]
                    if (scroll) element.scrollIntoView(scrollString)
                    errors.addAll(objectShouldLoadRequired(element, end, model, lang))
                    i += 1
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
            elementName: String,
            scroll: Boolean,
            scrollString: String,
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                if (scroll) widgetsCollection.first().scrollIntoView(scrollString)
                val filteredElements = widgetsCollection.filter(visible).shouldBe(sizeGreaterThan(0), timeout)
                val filteredElementsSize = filteredElements.size()
                val checked = "Checked at least one element $elementName in $klassName is visible (WidgetsCollection)"
                val collectionLog = widgetsCollection.toString().replace("\n", "\\n")
                logger.debug { "$checked: $collectionLog" }
                val errors = mutableListOf<Throwable>()
                // 'filteredElements' can change during the operation, so 'while' is used instead of 'forEach'
                var i = 0
                while (i < filteredElements.size() && i < filteredElementsSize) {
                    val widget = filteredElements[i]
                    if (scroll) widget.scrollIntoView(scrollString)
                    errors.addAll(objectShouldLoadRequired(widget, end, model, lang))
                    i += 1
                }
                errors.toList()
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
            elementName: String,
            scroll: Boolean,
            scrollString: String,
        ): List<Throwable> {
            val timeout = calculateTimeout(end)
            return try {
                val selenideElement = Page.find(element)
                if (scroll) selenideElement.scrollIntoView(scrollString)
                selenideElement.shouldBe(visible, timeout)
                val elementLog = element.toString().replace("\n", "\\n")
                logger.debug { "Checked element $elementName in $klassName is visible (WebElement): $elementLog" }
                objectShouldLoadRequired(element, end, model, lang)
            } catch (e: Throwable) {
                listOf(e)
            }
        }
    }
}

private fun <T : Loadable> T.errorsLoadingRequired(
    timeout: Duration, model: String,
    lang: String,
    scroll: Boolean,
    scrollString: String,
): List<Throwable> {
    val className = this::class.simpleName ?: "null"
    logger.debug { "Starting errorsLoadingRequired in class $className" }
    if (scroll) {
        when (this) {
            is By -> Page.find(this).scrollIntoView(scrollString)
            is SelenideElement -> this.scrollIntoView(scrollString)
            is ElementsCollection -> this.first().scrollIntoView(scrollString)
            is WebElement -> Page.find(this).scrollIntoView(scrollString)
        }
    }
    val end = LocalDateTime.now().plus(timeout)
    val errors = Loadable.objectShouldLoadRequired(this, end, model, lang).toMutableList()
    try {
        this.customShouldLoadRequired(timeout, model, lang)
    } catch (e: Throwable) {
        errors.add(e)
    }
    return errors.toList()
}

/**
 * All properties with [Required] annotation are checked if visible. Returns `this`.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality
 * (custom additional checks).
 *
 * @param timeout the timeout waiting for elements to become visible. Default value: Selenide's timeout
 * @param model the `model` used to check visibility. Default value: [SPConfig.model]
 * @param lang the `language` used to check visibility. Default value: [SPConfig.lang]
 * @param scroll if browser scrolls to the element before checking it. Default value: false
 * @param scrollString the string passed to [SelenideElement.scrollIntoView].
 * Default value: {behavior: "auto", block: "center", inline: "center"}
 * @throws RequiredError error can occur during validations (mostly, validation failures)
 * @return `this`, so it can be chained
 */
@Throws(RequiredError::class)
@JvmOverloads
fun <T : Loadable> T.shouldLoadRequired(
    timeout: Duration = Duration.ofMillis(SPConfig.selenideConfig.timeout()),
    model: String = SPConfig.model,
    lang: String = SPConfig.lang,
    scroll: Boolean = false,
    scrollString: String = "{behavior: \"auto\", block: \"center\", inline: \"center\"}",
): T {
    val errors = this.errorsLoadingRequired(timeout, model, lang, scroll, scrollString)
    if (errors.isNotEmpty()) throw RequiredError(errors)
    return this
}

/**
 * Returns `true` if [shouldLoadRequired] returns without throwing any exception, `false` otherwise.
 * You can override [Loadable.customShouldLoadRequired] method to add some extra functionality
 * (custom additional checks).
 *
 * @param timeout the timeout waiting for elements to become visible. Default value: Selenide's timeout
 * @param model the `model` used to check visibility. Default value: [SPConfig.model]
 * @param lang the `language` used to check visibility. Default value: [SPConfig.lang]
 * @param scroll if browser scrolls to the element before checking it. Default value: false
 * @param scrollString the string passed to [SelenideElement.scrollIntoView].
 * Default value: {behavior: "auto", block: "center", inline: "center"}
 * @return `true` if [shouldLoadRequired] returns without throwing any exception, `false` otherwise
 */
@Suppress("BooleanMethodIsAlwaysInverted")
@JvmOverloads
fun <T : Loadable> T.hasLoadedRequired(
    timeout: Duration = Duration.ofMillis(SPConfig.selenideConfig.timeout()),
    model: String = SPConfig.model,
    lang: String = SPConfig.lang,
    scroll: Boolean = false,
    scrollString: String = "{behavior: \"auto\", block: \"center\", inline: \"center\"}",
): Boolean {
    val errors = this.errorsLoadingRequired(timeout, model, lang, scroll, scrollString)
    return errors.isEmpty()
}
