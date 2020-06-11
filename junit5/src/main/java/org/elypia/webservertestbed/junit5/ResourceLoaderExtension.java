package org.elypia.webservertestbed.junit5;

import org.elypia.webservertestbed.TestUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.slf4j.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Extension for JUnit to cleanly load files from
 * the resource classpath.
 *
 * This allows resources to be loaded with a simple
 * {@link Resource} annotation to any of the following types:
 *
 * <ul>
 * <li>{@link String}</li>
 * <li>{@link InputStream}</li>
 * <li>{@link Byte[]}</li>
 * </ul>
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
public class ResourceLoaderExtension implements TestInstancePostProcessor {

    /** Logging with slf4j. */
    private static final Logger logger = LoggerFactory.getLogger(ResourceLoaderExtension.class);

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        logger.debug("Processing Test Instance: {}", testInstance);

        Class<?> testClazz = testInstance.getClass();
        Collection<Field> fields = AnnotationSupport.findAnnotatedFields(testClazz, Resource.class);

        for (Field field : fields) {
            if (field.get(testInstance) != null)
                continue;

            Resource resource = field.getAnnotation(Resource.class);
            String resourcePath = resource.value();

            Class<?> fieldClazz = field.getType();

            if (fieldClazz == InputStream.class)
                field.set(testInstance, TestUtils.getAsStream(resourcePath));
            else if (fieldClazz == String.class)
                field.set(testInstance, TestUtils.getAsString(resourcePath));
            else if (fieldClazz == byte[].class)
                field.set(testInstance, TestUtils.getAsBytes(resourcePath));
            else
                throw new IllegalStateException(Resource.class + " can not be used for field of type " + fieldClazz + ".");
        }
    }
}
