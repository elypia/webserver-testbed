package org.elypia.webservertestbed.junit5;

import org.elypia.webservertestbed.api.ContentLoader;
import org.elypia.webservertestbed.loaders.ResourceContentLoader;

import java.lang.annotation.*;

/**
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Response {

    /**
     * @return The body of response.
     */
    String value();

    /**
     * @return The HTTP response code of the request.
     */
    int responseCode() default 200;

    /**
     * @return The {@link ContentLoader} to use when
     * loading the {@link #value()}. Defaults to searching
     * for a resource in the classpath and reading it.
     */
    Class<? extends ContentLoader> loader() default ResourceContentLoader.class;
}
