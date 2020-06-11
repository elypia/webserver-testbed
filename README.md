# MockWebServer Testbed [![Matrix]][matrix-community] [![Discord]][discord-guild] [![Maven Central]][maven-page] [![Docs]][documentation] [![Build]][gitlab] [![Coverage]][gitlab] [![Donate]][elypia-donate]
The [Gradle]/[Maven] import strings can be found at the maven-central badge above!

## About
This is an extension for [MockWebServer] to include utilities for cleaner testing.
The goal of this project isn't to provide additional functionality to 
the existing OkHttp MockWebServer, but rather to allow a cleaner interface in 
test cases with less clunky or repetitive initialization code and integration
with modern test frameworks.

## Getting Started
### JUnit 5
The extension for JUnit 5 includes the starting and stopping of the MockWebServer
instance automatically, and creation of the MockResponses from resources
found in your classpath. You can use inline Strings if you prefer by setting
`Response#loader(Class<? extends ContentLoader>)` or 
`WebServerTest#loader(Class<? extends ContentLoader>)` to `LiteralContentLoader.class`,
or by making and providing a custom `ContentLoader` implementation.

#### Register Extension
```java
public class MyTest {
    
    /** 
    * Register the extension with JUnit 5. 
    * This can also be registered through ExtendWith; we recommend
    * using this approach though so you can access the mock webserver properties.
    */
    @RegisterExtension
    public static final WebServerExtension serverExtension = new WebServerExtension();
    	
    /**
    * Annotate your tests that require the webserver with
    * {@literal @WebServerTest} and specify the values to enqueue.
    * By default it'll look for resources in the classpath.
    */
    @WebServerTest("http_response.json")
    public void testHttpResponse() {
        MyApi api = new MyApi(serverExtension.getRequestUrl());
        api.makeApiRequest();
    }
    
    /** It also includes support for multiple requests/responses. */
    @WebServerTest({"http_response.json", "http_response_2.json"})
    public void testHttpResponse() {
        MyApi api = new MyApi(serverExtension.getRequestUrl());
        MyApiResponse response = api.makeApiRequest();
        api.makeOtherApiRequest(response.getProperty());
    }
}
```

#### Extend With
```java
/** 
* Register the extension with JUnit 5. 
* This is an alternative way to use the extension for flexibility.
* This is fine if you actively need to use the vanilla Test annotations available
* with JUnit; you'll just have to enqueue responses yourself.
*/
@ExtendWith(MockWebServerExtension.java)
public class MyTest {
    
    /** Get MockWebServer instance from the extension. */
    @WebServer
    public static MockWebServer server;
    
    /** A classpath resource and HTTP response status (default: 200). */
    @Response("http_response.json")
    public static MockResponse httpResponse;
    
    @Test
    public void testHttpResponse() {
        server.enqueue(httpResponse);
        
        // Make request and assert 
    }
}
```

### Other
The `org.elypia.webserver-testbed:core` module includes some generic utilities
that are used by all other modules. If we haven't made a direct integration for
your desired unit testing framework, you can just use this and call the
utility methods statically.

```java
public class MyTest {
    
    private static MockWebServer server;
        
    public void beforeAll() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    public void afterAll() throws IOException {
        server.close();
    }

    public void testLiteralResponse() {
        MockResponse response = Mock.of("{\"hello\": \"world\"}");
        server.enqueue(response);
    }
    
    public void testResourceResponse() {
        MockResponse response = Mock.of("http_response.json", new ResourceLoader());
        server.enqueue(response);
    }
    
    public void testMultipleReponses() {
        List<MockResponse> responses = Mock.ofAll("http_response.json", "http_response_2.json");
        
        for (MockResponse response : responses)
            server.enqueue(response);
    }
}
```

## Support
Should any problems occur, come visit us over on Discord! We're always around and there are
ample developers that would be willing to help; if it's a problem with the library itself then we'll
make sure to get it sorted.

[matrix-community]: https://matrix.to/#/+elypia:matrix.org "Matrix Invite"
[discord-guild]: https://discord.gg/hprGMaM "Discord Invite"
[maven-page]: https://search.maven.org/search?q=g:org.elypia.webserver-testbed "Maven Central"
[documentation]: https://elypia.gitlab.io/webserver-testbed "Documentation"
[gitlab]: https://gitlab.com/Elypia/webserver-testbed/commits/master "Repository on GitLab"
[elypia-donate]: https://elypia.org/donate "Donate to Elypia"
[Gradle]: https://gradle.org/ "Depend via Gradle"
[Maven]: https://maven.apache.org/ "Depend via Maven"
[MockWebServer]: https://github.com/square/okhttp/tree/master/mockwebserver "MockWebServer on GitHub"

[Matrix]: https://img.shields.io/matrix/elypia-general:matrix.org?logo=matrix "Matrix Shield"
[Discord]: https://discord.com/api/guilds/184657525990359041/widget.png "Discord Shield"
[Maven Central]: https://img.shields.io/maven-central/v/org.elypia.webserver-testbed/core "Download Shield"
[Docs]: https://img.shields.io/badge/docs-webserver-testbed-blue.svg "Documentation Shield"
[Build]: https://gitlab.com/Elypia/webserver-testbed/badges/master/pipeline.svg "GitLab Build Shield"
[Coverage]: https://gitlab.com/Elypia/webserver-testbed/badges/master/coverage.svg "GitLab Coverage Shield"
[Donate]: https://img.shields.io/badge/donate-elypia-blueviolet "Donate Shield"
