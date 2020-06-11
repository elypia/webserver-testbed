package org.elypia.webservertestbed.junit5;

import okhttp3.mockwebserver.*;
import org.elypia.webservertestbed.api.ContentLoader;
import org.elypia.webservertestbed.loaders.ResourceContentLoader;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

/**
 * A type of {@link Test} which calls {@link MockWebServer#enqueue(MockResponse)}
 * and creates a {@link MockResponse} with the annotation data.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
@Test
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServerTest {

    /**
     * This accepts one or more response values.
     * They will be #enqueued in the order that they
     * are given in the array.
     *
     * @return All response bodies for the test.
     */
    String[] value();

    /**
     * @return The {@link ContentLoader} to use when
     * loading the {@link #value()}. Defaults to searching
     * for a resource in the classpath and reading it.
     */
    Class<? extends ContentLoader> loader() default ResourceContentLoader.class;
}
