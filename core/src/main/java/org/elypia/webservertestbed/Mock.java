package org.elypia.webservertestbed;

import okhttp3.mockwebserver.MockResponse;
import org.elypia.webservertestbed.api.ContentLoader;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * Provides static methods for constructing {@link MockResponse} objects.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
public final class Mock {

    /** The default response code if it's not specified. */
    private static final int DEFAULT_RESPONSE_CODE = 200;

    private Mock() {
        throw new IllegalStateException("Don't construct this class.");
    }

    /**
     * @param responseBodies All response bodies to create MockResponses for.
     * @return Multiple MockResponses that can be provided to the MockWebServer.
     */
    public static List<MockResponse> ofAll(String... responseBodies) {
        List<MockResponse> responses = new ArrayList<>();

        for (String responseBody : responseBodies) {
            MockResponse response = of(responseBody);
            responses.add(response);
        }

        return responses;
    }

    /**
     * @param body The literal content to add to the response.
     * @return The MockResponse that can be provided to the MockWebServer.
     */
    public static MockResponse of(String body) {
        return of(body, DEFAULT_RESPONSE_CODE);
    }

    /**
     * @param value The string to load via the provided {@link ContentLoader}.
     * @param loader An implementation to load the body from the given value.
     * @return The MockResponse that can be provided to the MockWebServer.
     * @throws IOException If an exception occurs while loading the content.
     */
    public static MockResponse of(String value, ContentLoader loader) throws IOException {
        return of(value, DEFAULT_RESPONSE_CODE, loader);
    }

    /**
     * @param value The string to load via the provided {@link ContentLoader}.
     * @param responseCode The response code of the response.
     * @param loaderType The type of {@link ContentLoader} implementation,
     * this must have a default constructor.
     * @return The MockResponse that can be provided to the MockWebServer.
     * @throws IllegalAccessException If the default constructor isn't accesible by this method.
     * @throws InvocationTargetException If an error occurs trying to construct the {@link ContentLoader}.
     * @throws InstantiationException If the {@link ContentLoader} passed is an abstract class.
     * @throws IOException If an exception occurs while loading the content.
     */
    public static MockResponse of(String value, int responseCode, Class<? extends ContentLoader> loaderType) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Constructor<?> constructor = Stream.of(loaderType.getConstructors())
            .filter((c) -> c.getParameterCount() == 0)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Can only construct ContentLoaders with a default constructor."));

        ContentLoader loader = (ContentLoader)constructor.newInstance();
        return of(value, responseCode, loader);
    }

    /**
     * @param value The string to load via the provided {@link ContentLoader}.
     * @param responseCode The response code of the response.
     * @param loader An implementation to load the body from the given value.
     * @return The MockResponse that can be provided to the MockWebServer.
     * @throws IOException If an exception occurs while loading the content.
     */
    public static MockResponse of(String value, int responseCode, ContentLoader loader) throws IOException {
        String content = loader.loadContent(value);
        return of(content, responseCode);
    }

    /**
     * @param body The literal content to add to the response.
     * @param responseCode The response code of the response.
     * @return The MockResponse that can be provided to the MockWebServer.
     */
    public static MockResponse of(String body, int responseCode) {
        return new MockResponse().setResponseCode(responseCode).setBody(body);
    }
}
