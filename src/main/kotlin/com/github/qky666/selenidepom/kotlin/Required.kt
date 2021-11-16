package com.github.qky666.selenidepom.kotlin

/**
 * Every property with @Required annotation in a RequiredContainer will be checked
 * if visible when shouldLoadRequired method is called.
 */
@MustBeDocumented
@Target(AnnotationTarget.PROPERTY)
annotation class Required
