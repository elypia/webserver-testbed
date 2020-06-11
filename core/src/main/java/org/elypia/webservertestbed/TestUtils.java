/*
 * Copyright 2020-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.webservertestbed;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Utility methods for testing.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
public final class TestUtils {

    private TestUtils() {
        throw new IllegalStateException("Don't construct this class.");
    }

    /**
     * @param name The path to the resource to load.
     * @return The InputStream of the resource located at the specified path
     * relative to the root of the classpath.
     * @throws NullPointerException If the resource doesn't exist.
     */
    public static InputStream getAsStream(String name) {
        Objects.requireNonNull(name);
        String path = File.separator + name;

        InputStream stream = TestUtils.class.getResourceAsStream(path);
        Objects.requireNonNull(stream, "Resource at `" + path + "`" + " doesn't exist.");

        return stream;
    }

    public static byte[] getAsBytes(String name) throws IOException {
        try (InputStream stream = getAsStream(name)) {
            return stream.readAllBytes();
        }
    }

    /**
     * Load all content from the file specified.
     * Path should be relative to the root of the classpath.
     *
     * @param name The path to the resource to load.
     * @return The content of the file.
     */
    public static String getAsString(String name) throws IOException {
        try (InputStream stream = getAsStream(name)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
