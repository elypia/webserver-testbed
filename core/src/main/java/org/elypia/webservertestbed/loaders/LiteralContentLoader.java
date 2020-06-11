package org.elypia.webservertestbed.loaders;

import org.elypia.webservertestbed.api.ContentLoader;

/**
 * Returns the literal content of the input.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
public class LiteralContentLoader implements ContentLoader {

    @Override
    public String loadContent(String value) {
        return value;
    }
}
