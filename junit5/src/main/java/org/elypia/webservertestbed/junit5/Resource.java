package org.elypia.webservertestbed.junit5;

import java.lang.annotation.*;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resource {

    /**
     * @return The path of the resource relative to the
     * root of the classpath.
     */
    String value();
}
