package org.elypia.webservertestbed.api;

import okhttp3.mockwebserver.MockResponse;

import java.io.IOException;

/**
 * Defines how content is to be loaded from a {@link String} input.
 * The desired {@link ContentLoader} is created through reflection
 * so it must always have a paramterless constructor available.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
@FunctionalInterface
public interface ContentLoader {

    /**
     * @param value The literal string that represents a resource that should be loaded.
     * @return The content to load from a given string intended
     * to be added to {@link MockResponse#setBody(String)}.
     * @throws IOException If an exception occurs while loading the content.
     */
    String loadContent(String value) throws IOException;
}
