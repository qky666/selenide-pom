package com.github.qky666.selenidepom.pom

import com.codeborne.selenide.*
import java.time.Duration

/**
 * Abstract class that implements [Loadable] interface based on a [ElementsCollection].
 */
abstract class WidgetsCollection<ThisType : WidgetsCollection<ThisType>>(val elementsCollection: ElementsCollection) :
    Loadable {

    // TODO: Incomplete class...

    private fun thisWithType(): ThisType {
        @Suppress("UNCHECKED_CAST")
        return this as ThisType
    }

    fun should(vararg conditions: CollectionCondition): ThisType {
        elementsCollection.should(*conditions)
        return thisWithType()
    }

    fun should(condition: CollectionCondition, timeout: Duration): ThisType {
        elementsCollection.should(condition, timeout)
        return thisWithType()
    }

    fun shouldBe(vararg conditions: CollectionCondition): ThisType {
        elementsCollection.shouldBe(*conditions)
        return thisWithType()
    }

    fun shouldBe(condition: CollectionCondition, timeout: Duration): ThisType {
        elementsCollection.shouldBe(condition, timeout)
        return thisWithType()
    }

    fun shouldHave(vararg conditions: CollectionCondition): ThisType {
        elementsCollection.shouldHave(*conditions)
        return thisWithType()
    }

    fun shouldHave(condition: CollectionCondition, timeout: Duration): ThisType {
        elementsCollection.shouldHave(condition, timeout)
        return thisWithType()
    }
}
