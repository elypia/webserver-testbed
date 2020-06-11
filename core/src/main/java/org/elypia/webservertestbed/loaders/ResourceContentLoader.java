package org.elypia.webservertestbed.loaders;

import org.elypia.webservertestbed.TestUtils;
import org.elypia.webservertestbed.api.ContentLoader;

import java.io.IOException;

/**
 * Read the content by grabbing the resource relative
 * to the root of the classpath of the name of the value.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
public class ResourceContentLoader implements ContentLoader {

    @Override
    public String loadContent(String value) throws IOException {
        return TestUtils.getAsString(value);
    }
}
