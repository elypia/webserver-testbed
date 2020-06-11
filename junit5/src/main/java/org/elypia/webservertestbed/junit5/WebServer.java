package org.elypia.webservertestbed.junit5;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.extension.*;

import java.lang.annotation.*;

/**
 * <p>
 *   Acts as an injection point for {@link MockWebServer} instance
 *   if the {@link WebServerExtension} is registered to the test instance.
 * </p>
 * <p>
 *   This is helpful when registring the {@link Extension} via
 *   {@link ExtendWith}.
 * </p>
 * <p>
 *   <strong>
 *     This is available for flexibility, if suitable for your requirements
 *     it's recommended to use {@link RegisterExtension} to register this.
 *   </strong>
 * </p>
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServer { }
