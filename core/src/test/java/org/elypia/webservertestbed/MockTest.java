package org.elypia.webservertestbed;

import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockTest {

    @Test
    public void testLoadingLiteral() {
        MockResponse response = Mock.of("Hello, world!");

        String expected = "Hello, world!";
        String actual = response.getBody().readString(StandardCharsets.UTF_8);

        assertEquals(expected, actual);
    }
}
