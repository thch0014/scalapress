package com.cloudray.scalapress;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Stephen Samuel
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Tag {
    String value();
}
