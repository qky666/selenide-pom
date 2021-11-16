package com.github.qky666.selenidepom.kotlin

/**
 * Every property annotated in a class that implements [com.github.qky666.selenidepom.kotlin.RequiredContainer]
 * will be checked if visible when one of the methods defined in the interface is called.
 */
@MustBeDocumented
@Target(AnnotationTarget.PROPERTY)
annotation class Required
