package com.github.qky666.selenidepom.java;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.time.Duration;

/**
 * Every field or method (with no parameters) with this annotation in a
 * {@link com.github.qky666.selenidepom.java.RequiredContainer} will be checked if visible when
 * {@link com.github.qky666.selenidepom.java.RequiredContainer#shouldLoadRequired(Duration)} or
 * {@link com.github.qky666.selenidepom.java.RequiredContainer#shouldLoadRequired()} method is called.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({
        ElementType.FIELD,
        ElementType.METHOD
})
public @interface Required {
}
