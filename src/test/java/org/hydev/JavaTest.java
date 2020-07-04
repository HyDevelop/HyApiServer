package org.hydev;

import okhttp3.*;
import okhttp3.Request.Builder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static okhttp3.MediaType.parse;
import static okhttp3.RequestBody.create;

@SuppressWarnings({"ConstantConditions"})
public class JavaTest
{
    static ApiServer server;
    static OkHttpClient http = new OkHttpClient();
    static RequestBody body = create("I'm a cat", parse("text/plain"));

    @BeforeAll
    static void init()
    {
        server = new ApiServer(1029);
        server.getNodes().register(new JavaExampleNode());
        server.start();
    }

    @Test
    void testWrongMethod() throws IOException
    {
        // Set custom server config
        server.getAcceptedMethods().remove(1);
        server.setHandleWrongMethod(it -> { it.write("Hahahahahahhah"); return null; });

        Request request = new Builder().post(body).url("http://localhost:1029/api/echo").build();
        try (Response response = http.newCall(request).execute())
        {
            server.getAcceptedMethods().add("post");
            assert response.body().string().equals("Hahahahahahhah");
        }
    }

    @Test
    void testNodeNotFound() throws IOException
    {
        Request request = new Builder().url("http://localhost:1029/c/").build();
        try (Response response = http.newCall(request).execute())
        {
            assert response.body().string().equals("Not found.");
            assert response.code() == 404;
        }
    }

    @Test
    void testWithoutContent() throws IOException
    {
        Request request = new Builder().url("http://localhost:1029/api/echo").build();
        try (Response response = http.newCall(request).execute())
        {
            assert response.body().string().equals("What do you want me to say?");
        }
    }

    @Test
    void testWithoutHeader() throws IOException
    {
        Request request = new Builder().post(body).url("http://localhost:1029/api/echo").build();
        try (Response response = http.newCall(request).execute())
        {
            assert response.body().string().equals("Are you just going to ask me like that? Ծ‸Ծ");
        }
    }

    @Test
    void testWithCorrectHeader() throws IOException
    {
        Request request = new Builder().post(body).header("cute", "yes").url("http://localhost:1029/api/echo").build();
        try (Response response = http.newCall(request).execute())
        {
            assert response.body().string().equals("Thank you! (⺣◡⺣)\nI'm a cat");
        }
    }
}
