package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.BaseElementsCollection
import com.codeborne.selenide.ElementsCollection
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebElementCondition
import com.codeborne.selenide.WebElementsCondition
import com.codeborne.selenide.impl.CollectionSource
import java.time.Duration

/**
 * Almost the same as [ElementsCollection], but most methods return a [Widget] instead of a [SelenideElement].
 *
 * @param elementsCollection the underlying [ElementsCollection]
 * @param factory the [T] ([Widget] subclass) constructor
 * @constructor creates a new instance based on provided [elementsCollection] and [T] constructor
 */
class WidgetsCollection<T : Widget>(
    private val elementsCollection: ElementsCollection,
    private val factory: (e: SelenideElement) -> T,
) : ElementsCollection(elementsCollection.getCollectionSource()), Loadable {

    /**
     * [CollectionSource] associated to this instance.
     */
    val collectionSource
        get() = elementsCollection.getCollectionSource()

    // ElementsCollection overrides
    /**
     * Gets the n-th element of collection (lazy evaluation)
     *
     * @param index 0..N
     * @return the n-th element of collection
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    override fun get(index: Int) = super.get(index).asWidget(factory)

    /**
     * Find the first element which met the given condition (lazy evaluation)
     *
     * @param condition condition
     * @return SelenideElement
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    override fun find(condition: WebElementCondition) = super.find(condition).asWidget(factory)

    /**
     * Find the first element which met the given condition (lazy evaluation)
     *
     * @param condition condition
     * @return SelenideElement
     * @see .find
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    override fun findBy(condition: WebElementCondition) = super.findBy(condition).asWidget(factory)

    /**
     * Returns the first element of the collection (lazy evaluation)
     *
     * NOTICE: Instead of `$$(css).first()`, prefer `$(css)` as it's faster and returns the same result
     *
     * @return the first element of the collection
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    override fun first(): T = super.first().asWidget(factory)

    /**
     * Returns the last element of the collection (lazy evaluation)
     *
     * @return the last element of the collection
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    override fun last() = super.last().asWidget(factory)

    /**
     * returns the first n elements of the collection (lazy evaluation)
     *
     * @param elements number of elements 1..N
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    @Suppress("GrazieInspection")
    override fun first(elements: Int) = WidgetsCollection(super.first(elements), factory)

    /**
     * returns the last n elements of the collection (lazy evaluation)
     *
     * @param elements number of elements 1..N
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    @Suppress("GrazieInspection")
    override fun last(elements: Int) = WidgetsCollection(super.last(elements), factory)

    /**
     * Check if a collection matches given condition(s).
     *
     *  For example:
     *
     * <pre>
     * `$$(".text_list").should(containExactTextsCaseSensitive("text1", "text2"));
     * $$(".cat_list").should(allMatch("value==cat", el -> el.getAttribute("value").equals("cat")));
    ` *
    </pre> *
     */
    override fun should(vararg conditions: WebElementsCondition) = WidgetsCollection(super.should(*conditions), factory)

    /**
     * Check if a collection matches a given condition within the given time period.
     *
     * @param timeout maximum waiting time
     */
    override fun should(condition: WebElementsCondition, timeout: Duration) =
        WidgetsCollection(super.should(condition, timeout), factory)

    override fun should(
        prefix: String,
        timeout: Duration,
        vararg conditions: WebElementsCondition,
    ) = WidgetsCollection(super.should(prefix, timeout, *conditions), factory)

    /**
     * For example: `$$(".error").shouldBe(empty)`
     */
    override fun shouldBe(vararg conditions: WebElementsCondition) =
        WidgetsCollection(super.shouldBe(*conditions), factory)

    override fun shouldBe(condition: WebElementsCondition, timeout: Duration) =
        WidgetsCollection(super.shouldBe(condition, timeout), factory)

    /**
     * For example:
     * `$$(".error").shouldHave(size(3))`
     * `$$(".error").shouldHave(texts("Error1", "Error2"))`
     */
    override fun shouldHave(vararg conditions: WebElementsCondition) =
        WidgetsCollection(super.shouldHave(*conditions), factory)

    /**
     * Check if a collection matches given condition within given period
     *
     * @param timeout maximum waiting time
     */
    override fun shouldHave(condition: WebElementsCondition, timeout: Duration) =
        WidgetsCollection(super.shouldHave(condition, timeout), factory)

    /**
     * Filters collection elements based on the given condition (lazy evaluation)
     *
     * @param condition condition
     * @return ElementsCollection
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    override fun filter(condition: WebElementCondition) = WidgetsCollection(super.filter(condition), factory)

    /**
     * Filters collection elements based on the given condition (lazy evaluation)
     *
     * @param condition condition
     * @return ElementsCollection
     * @see .filter
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    override fun filterBy(condition: WebElementCondition) = WidgetsCollection(super.filterBy(condition), factory)

    /**
     * Filters elements excluding those which met the given condition (lazy evaluation)
     *
     * @param condition condition
     * @return ElementsCollection
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    override fun exclude(condition: WebElementCondition) = WidgetsCollection(super.exclude(condition), factory)

    /**
     * Filters elements excluding those which met the given condition (lazy evaluation)
     *
     * @param condition condition
     * @return ElementsCollection
     * @see .exclude
     * @see [Lazy loading](https://github.com/selenide/selenide/wiki/lazy-loading)
     */
    override fun excludeWith(condition: WebElementCondition) = WidgetsCollection(super.excludeWith(condition), factory)

    /**
     * Takes the snapshot of current state of this collection.
     * Succeeding calls to this object WILL NOT RELOAD collection element from browser.
     *
     *
     * Use it to speed up your tests - but only if you know that collection will not be changed during the test.
     *
     * @return current state of this collection
     * @see .asFixedIterable
     */
    override fun snapshot() = WidgetsCollection(super.snapshot(), factory)

    /**
     * Give this collection a human-readable name
     *
     *
     * Caution: you probably don't need this method.
     * It's always a good idea to have the actual selector instead of "nice" description
     * (which might be misleading or even lying).
     *
     * @param alias a human-readable name of this collection (null or empty string not allowed)
     * @return this collection
     * @since 5.20.0
     */
    override fun `as`(alias: String) = WidgetsCollection(super.`as`(alias), factory)
}

/**
 * Returns the [CollectionSource] associated with this [ElementsCollection].
 *
 * Note: It is a private field, so we have to obtain it through reflexion.
 *
 * @return The [CollectionSource] associated
 */
fun <T : ElementsCollection> T.getCollectionSource(): CollectionSource {
    if (this is WidgetsCollection<*>) return this.collectionSource
    val field = BaseElementsCollection::class.java.getDeclaredField("collection")
    field.isAccessible = true
    return field.get(this) as CollectionSource
}
