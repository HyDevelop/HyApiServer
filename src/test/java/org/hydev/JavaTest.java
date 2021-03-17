package org.hydev;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.hydev.KotlinExampleJsonNode.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.lang.Double.parseDouble;
import static java.lang.Math.floor;
import static okhttp3.MediaType.parse;
import static okhttp3.RequestBody.create;

@SuppressWarnings({"ConstantConditions"})
public class JavaTest {
    static ApiServer server;
    static OkHttpClient http = new OkHttpClient();
    static RequestBody body = create("I'm a cat", parse("text/plain"));

    @BeforeAll
    static void init() {
        server = new ApiServer(1029);
        server.getNodes().register(new JavaExampleNode(), new JavaExampleJsonNode());
        server.start();
    }

    @Test
    void testWrongMethod() throws IOException {
        // Set custom server config
        server.getAcceptedMethods().remove(1);
        server.setHandleWrongMethod(it -> {
            it.write("Hahahahahahhah");
            return null;
        });

        Request request = new Builder().post(body).url("http://localhost:1029/api/echo").build();
        try (Response response = http.newCall(request).execute()) {
            server.getAcceptedMethods().add("post");
            assert response.body().string().equals("Hahahahahahhah");
        }
    }

    @Test
    void testNodeNotFound() throws IOException {
        Request request = new Builder().url("http://localhost:1029/c/").build();
        try (Response response = http.newCall(request).execute()) {
            assert response.body().string().equals("{\"error\":\"Not found.\"}");
            assert response.code() == 404;
        }
    }

    @Test
    void testWithoutContent() throws IOException {
        Request request = new Builder().url("http://localhost:1029/api/echo").build();
        try (Response response = http.newCall(request).execute()) {
            assert response.body().string().equals("What do you want me to say?");
        }
    }

    @Test
    void testWithoutHeader() throws IOException {
        Request request = new Builder().post(body).url("http://localhost:1029/api/echo").build();
        try (Response response = http.newCall(request).execute()) {
            assert response.body().string().equals("Are you just going to ask me like that? Ծ‸Ծ");
        }
    }

    @Test
    void testWithCorrectHeader() throws IOException {
        Request request = new Builder().post(body).header("cute", "yes").url("http://localhost:1029/api/echo").build();
        try (Response response = http.newCall(request).execute()) {
            assert response.body().string().equals("Thank you! (⺣◡⺣)\nI'm a cat");
        }
    }

    @Test
    void testJson() throws IOException {
        Request request = new Builder()
                .post(create(new Gson().toJson(new Model(23, 33)), parse("application/json")))
                .url("http://localhost:1029/json/divide").build();

        try (Response response = http.newCall(request).execute()) {
            assert floor(parseDouble(response.body().string()) * 100) == 69;
        }
    }

    @Test
    void testJsonWithoutBody() throws IOException {
        Request request = new Builder()
                .url("http://localhost:1029/json/divide").build();

        try (Response response = http.newCall(request).execute()) {
            assert response.body().string().equals("{\"error\":\"Request body is empty.\"}");
        }
    }

    @Test
    void testJsonWithIncorrectModel() throws IOException {
        Request request = new Builder()
                .post(create("{\"name\": \"Yukari Yakumo\", \"age\": \"At least 1200anghhkjlhjhgfsdfhjklfc\"}",
                        parse("application/json")))
                .url("http://localhost:1029/json/divide").build();

        try (Response response = http.newCall(request).execute()) {
            assert response.body().string().equals("NaN");
        }
    }

    @Test
    void testJsonWithUnparseableModel() throws IOException {
        Request request = new Builder()
                .post(create("The quick brown fox jumps over the lazy dog.",
                        parse("application/json")))
                .url("http://localhost:1029/json/divide").build();

        try (Response response = http.newCall(request).execute()) {
            assert response.body().string().startsWith("{\"error\":\"Error during json parsing: ");
        }
    }

    @Test
    void testJsonWithDataTooLong() throws IOException {
        Request request = new Builder()
                .post(create("The quick brown fox didn't jump over the lazy dog, but decided to slide under the lazy dog this time.",
                        parse("application/json")))
                .url("http://localhost:1029/json/divide").build();

        try (Response response = http.newCall(request).execute()) {
            assert response.body().string().equals("{\"error\":\"Body too long. (101/80)\"}");
        }
    }

    @Test
    void testJsonWithInternalError() throws IOException {
        Request request = new Builder()
                .post(create(new Gson().toJson(new Model(1337, 0)), parse("application/json")))
                .url("http://localhost:1029/json/divide").build();

        try (Response response = http.newCall(request).execute()) {
            assert response.body().string().equals("Infinity");
        }
    }
}
