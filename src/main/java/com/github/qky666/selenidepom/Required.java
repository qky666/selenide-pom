package com.github.qky666.selenidepom;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Every field or method (with no parameters) with @Required annotation in a Page or a Widget will be checked
 * if visible when shouldLoadRequired method is called.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Required {
}
