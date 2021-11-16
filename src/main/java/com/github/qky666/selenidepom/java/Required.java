package com.github.qky666.selenidepom.java;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.time.Duration;

/**
 * Every field or method (without parameters) annotated in a class that implements
 * {@link com.github.qky666.selenidepom.java.RequiredContainer} will be checked if visible when
 * one of the methods defined in the interface is called.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({
        ElementType.FIELD,
        ElementType.METHOD
})
public @interface Required {
}
