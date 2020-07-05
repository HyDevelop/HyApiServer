# JettyApiServer [![](https://jitpack.io/v/HyDevelop/JettyServer.svg)](https://jitpack.io/#HyDevelop/JettyServer)  
This is a lightweight Kotlin (Java) library for creating http servers with API nodes (or paths), 
designed for small scale microservices.
It is implemented with [Jetty](https://www.eclipse.org/jetty/), 
a lightweight http server library.
This library is only around 600 KB along with the dependencies,
and it can run directly using `java -jar` without any heavy containers like Tomcat.
However, I do recommend using Nginx on top of it if you want to run multiple services on one machine.

## How to use

### 1. Import

TODO!

### 2. Create an API server

Just construct an `ApiServer` object with your host port.

#### Kotlin:

```kotlin
fun main()
{
    val server = ApiServer(1029)

    // To register api or Json api nodes 
    // with the POWER OF KOTLIN:
    // (See below for how to create api nodes)
    server.nodes += YourApiNode()
    
    // This works too:
    server.nodes.register(YourOtherApiNode())

    // To start the server on this thread:
    server.startSync()

    // To start the server on a separate thread 
    // if you want to do something else on this thread:
    // server.start()
}
```

#### Java:

`````Java
public class JavaExample
{
    public static void main(String[] args)
    {
        ApiServer server = new ApiServer(1029);

        // Register api nodes (see below for how to create them):
        server.nodes.register(new YourOtherApiNode());
        
        // To start the server on this thread:
        server.startSync();
    
        // To start the server on a separate thread 
        // if you want to do something else on this thread:
        // server.start();
    }
}
`````

### 3. Create Simple API Nodes

As simple as extending the `ApiNode` class and specifying a path.
After registering the node, 
the `process()` function would be called every time a user accesses the path.

Note: Don't forget to register your api nodes!
(You can find how to register nodes in section #2)

#### Kotlin:

```kotlin
// This path is relative to the server's host location, 
// so this example path can be accessed from "http://host:port/path/to/your/api"
// Additional parameters includes:
// - isSecure (defaults to false): 
//   Whether the access information would be outputted when errors occur.
class KotlinApiNode: ApiNode("/path/to/your/api")
{
    override fun process(access: ApiAccess): Any?
    {
        // Read headers:
        val header = access.headers["header-name"]
        
        // Read url parameters:
        val param = access.request.getParameter("param-name")
        
        // Read body content 
        val body = access.body
        
        // Customize response
        access.response.setHeader("some-header", "value")
        
        // Write response by either returning the value or using access.write()
        // Note: Returning null wouldn't write "null", as it would write nothing.
        return "Hi there!"
    }
}
```

#### Java:

```java
public class JavaExampleNode extends ApiNode
{
    public JavaExampleNode()
    {
        // For detailed documentation of what these two parameters mean,
        // please look at the comments on the Kotlin example above.
        super("/path/to/your/api", false);
    }

    @Override
    public Object process(@NotNull ApiAccess access)
    {
        // Read headers:
        String header = access.getHeaders().get("header-name");
        
        // Read url parameters:
        String param = access.getRequest().getParameter("param-name");
        
        // Read body content 
        String body = access.getBody();
        
        // Customize response
        access.getResponse().setHeader("some-header", "value");
        
        // Write response by either returning the value or using access.write()
        // Note: Returning null wouldn't write "null", as it would write nothing.
        return "Hi there!";
    }
}
```

### 4. Create JSON Api Nodes:

This library also has features for automatic parsing and error-handling for json api nodes.
You need to provide a model class for incoming json, but the parsing is automatic 
(see examples below).

Note: Don't forget to register your api nodes!
(You can find how to register nodes in section #2)


#### Kotlin:

```kotlin
// The path is the same as a regular api node, but you need to specify
// the class for the data model because it's required in Json parsing.
// Additional parameters includes:
// - isSecret: same as ApiNode's isSecret
// - maxLength (default is 10000): The maximum length of the json passed in.
// - parser (default is Json(Stable)): The json parser used.
class KotlinExampleJsonNode: JsonApiNode<KotlinExampleJsonNode.Model>("/json/divide", Model::class, maxLength = 80)
{
    /**
     * Model of the data passed in through request body.
     */
    @Serializable
    data class Model(val dividend: Double, val divisor: Double)

    /**
     * Process json data which is already automatically parsed. 
     * In this case, this api does a simple division of the two numbers given.
     *
     * Note: If you return an object of a class, it would be automatically 
     * stringified as an Json object, but if you return a String or a primitive,
     * it would be returned directly.
     */
    override fun json(access: ApiAccess, data: Model) = data.dividend / data.divisor
}
```

#### Java:

Since Java doesn't have a single best-performing way of doing Json parsing,
everyone prefers different parsers, 
so you have to implement the parser class with your desired Json library.
I recommend putting the parser initialization in your Main class before you start the server.

Here is how to do it with Gson:

```java
/*
 * Create parser (You only have to do this only once)
 */
Gson gson = new Gson();
JsonApiNodeJava.parser = new JavaJsonParser()
{
    @Override
    public <T> T parse(@NotNull Class<T> type, @NotNull String json)
    {
        return gson.fromJson(json, type);
    }

    @NotNull
    @Override
    public <T> String stringify(@NotNull Class<T> type, @NotNull Object data)
    {
        return gson.toJson(data);
    }
};
```

Then you can move on to creating the actual api node:

```Java
public class JavaExampleJsonNode extends JsonApiNodeJava<JavaExampleJsonNode.Model>
{
    /**
     * Create node
     */
    public JavaExampleJsonNode()
    {
        // (Path, Model class, Is secret, Max length).
        // Details see Kotlin example above.
        super("/json/divide", Model.class, false, 80);
    }

    /**
     * Model of the data passed in through request body.
     */
    static class Model
    {
        double dividend;
        double divisor;
    }

    /**
     * Process json data which is already automatically parsed. 
     * In this case, this api does a simple division of the two numbers given.
     *
     * Note: If you return an object of a class, it would be automatically 
     * stringified as an Json object, but if you return a String or a primitive,
     * it would be returned directly.
     */
    @Override
    public Object json(@NotNull ApiAccess access, Model data)
    {
        return data.dividend / data.divisor;
    }
}
```

### 5. More Advanced Setup (Optional):

If you want to customize the setup, you can modify the fields in your `server` object in your Main function.

What you can change:

* How are different errors handled.
* What errors are suppressed. (Default only KnownException)
* What are the accepted methods. (Default "get" and "post")
* How is the default response configured.

For example, if you want to disable the "Ignored error" output:

```kotlin
// Copy the code from ApiServer.kt, and delete the println line.
server.handleSuppressedError = { access: ApiAccess, e: Exception -> access.write(jsonError(e.message))}
```

For example #2, if you want to turn on CORS limitations:

```kotlin
// This is the function that the server calls when configuring default responses.
server.configureResponse = {
    it.status = SC_OK
    it.contentType = "application/json; charset=utf-8"
    it.setHeader("Access-Control-Allow-Origin", "*") // Change this to something you want.
    it.setHeader("Access-Control-Allow-Credentials", "true")
}
```

If you want to do anything else, just read the code!
