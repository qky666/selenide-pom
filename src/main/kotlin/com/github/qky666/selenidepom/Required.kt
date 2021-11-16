package com.github.qky666.selenidepom

/**
 * Every property, field or method (no parameters) with @Required annotation in a RequiredContainer will be checked
 * if visible when shouldLoadRequired method is called.
 */
@MustBeDocumented
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
)
annotation class Required
