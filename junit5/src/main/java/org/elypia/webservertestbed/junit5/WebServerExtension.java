package org.elypia.webservertestbed.junit5;

import okhttp3.mockwebserver.*;
import org.elypia.webservertestbed.Mock;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.slf4j.*;

import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;

/**
 * <p>
 *   Extension for JUnit to create {@link MockResponse}
 *   through annotations.
 * </p>
 * <p>
 *   The following is an example on the simplest usage of this extention:
 *   <pre><code>
 * public class MyTest {
 *
 *      // Register the extension, and retain a reference to it if you need any data from it.
 *     {@literal @RegisterExtension}
 *      public static final WebServerExtension serverExtension = new WebServerExtension();
 *
 *     {@literal @WebServerTest("api_response.json")}
 *      public void testMyApi() {
 *          MyApi api = new MyApi(serverExtension.getRequestUrl());
 *          MyResponseObject response = myApi.makeApiRequest();
 *      }
 *  }</code></pre>
 * </p>
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 1.0.0
 */
public class WebServerExtension implements TestInstancePostProcessor, BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback, ParameterResolver {

    /** Logging with slf4j. */
    private static final Logger logger = LoggerFactory.getLogger(WebServerExtension.class);

    /**
     * If to {@link MockWebServer#start()} and {@link MockWebServer#close()}
     * between every test case, instead of before all, and after all.
     */
    private final boolean beforeEach;

    public WebServerExtension() {
        this(false);
    }

    /**
     * @param beforeEach By default the mock webserver is started
     * {@link #beforeAll(ExtensionContext)} and closed {@link #afterAll(ExtensionContext)}.
     * Set this to true to make it {@link #beforeEach(ExtensionContext)} and {@link #afterEach(ExtensionContext)} instead.
     */
    public WebServerExtension(boolean beforeEach) {
        this.beforeEach = beforeEach;
        mockWebServer = new MockWebServer();
    }

    /**
     * The underlying mockwebserver implementation this extension abstracts.
     */
    private MockWebServer mockWebServer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!beforeEach)
            mockWebServer.start();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (!beforeEach)
            mockWebServer.close();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (beforeEach)
            mockWebServer.start();

        Optional<Method> optMethod = context.getTestMethod();

        if (optMethod.isPresent()) {
            Method method = optMethod.get();
            Optional<WebServerTest> optTest = AnnotationSupport.findAnnotation(method, WebServerTest.class);

            if (optTest.isPresent()) {
                WebServerTest test = optTest.get();

                for (String content : test.value()) {
                    MockResponse mockResponse = Mock.of(content, 200, test.loader());
                    mockWebServer.enqueue(mockResponse);
                }
            }
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (beforeEach)
            mockWebServer.close();
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Class<?> testClazz = testInstance.getClass();
        setMockWebServerInstance(testInstance, testClazz);
        setMockResponseInstances(testInstance, testClazz);
    }

    private void setMockWebServerInstance(Object testInstance, Class<?> testClazz) throws IllegalAccessException {
        List<Field> mockWebServerFields = AnnotationSupport.findPublicAnnotatedFields(testClazz, MockWebServer.class, WebServer.class);

        if (mockWebServerFields.isEmpty())
            return;

        if (mockWebServerFields.size() > 1)
            logger.warn("There is no reason to inject the {} more than once.", MockWebServer.class);

        for (Field mockWebServerField : mockWebServerFields)
            mockWebServerField.set(testInstance, mockWebServer);
    }

    private void setMockResponseInstances(Object testInstance, Class<?> testClazz) throws IllegalAccessException, InstantiationException, IOException, InvocationTargetException {
        Collection<Field> mockResponseFields = AnnotationSupport.findPublicAnnotatedFields(testClazz, MockResponse.class, Response.class);

        for (Field field : mockResponseFields) {
            if (field.get(testInstance) != null)
                continue;

            Response response = field.getAnnotation(Response.class);
            MockResponse mockResponse = Mock.of(response.value(), response.responseCode(), response.loader());

            field.set(testInstance, mockResponse);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return MockWebServer.class == parameterContext.getParameter().getType();
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return mockWebServer;
    }

    /**
     * @return The root address of the mock webserver.
     */
    public URL getRequestUrl() {
        return getRequestUrl("/");
    }

    /**
     *
     * @param path The path relative of the server root location.
     * @return The address of the server to the specific path.
     */
    public URL getRequestUrl(String path) {
        Objects.requireNonNull(path);
        return mockWebServer.url(path).url();
    }

    /**
     * @return The undelying mock webserver instance.
     */
    public MockWebServer getServer() {
        return mockWebServer;
    }
}
