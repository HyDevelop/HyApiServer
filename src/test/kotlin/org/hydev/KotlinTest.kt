package org.hydev

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request.Builder
import okhttp3.RequestBody.Companion.toRequestBody
import org.hydev.KotlinExampleJsonNode.Model
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.IOException
import kotlin.math.floor

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KotlinTest
{
    private val server: ApiServer = ApiServer(1029)
    private val http = OkHttpClient()
    private val body = "I'm a cat".toRequestBody("text/plain".toMediaType());

    @BeforeAll
    fun init()
    {
        // Register with the .register() method
        server.nodes.register(KotlinExampleNode())

        // Register with += operator
        server.nodes += KotlinExampleJsonNode()

        server.start()
    }

    @Test
    fun testWrongMethod()
    {
        // Set custom server config
        server.acceptedMethods.removeAt(1)
        server.handleWrongMethod = { it.write("Hahahahahahhah") }
        val request = Builder().post(body).url("http://localhost:1029/api/echo").build()

        http.newCall(request).execute().use {
            server.acceptedMethods.add("post")
            assert(it.body!!.string() == "Hahahahahahhah")
        }
    }

    @Test
    fun testNodeNotFound()
    {
        val request = Builder().url("http://localhost:1029/c/").build()
        http.newCall(request).execute().use { response ->
            assert(response.body!!.string() == "{\"error\":\"Not found.\"}")
            assert(response.code == 404)
        }
    }

    @Test
    fun testWithoutContent()
    {
        val request = Builder().url("http://localhost:1029/api/echo").build()
        http.newCall(request).execute()
            .use { assert(it.body!!.string() == "What do you want me to say?") }
    }

    @Test
    fun testWithoutHeader()
    {
        val request =
            Builder().post(body).url("http://localhost:1029/api/echo").build()
        http.newCall(request).execute()
            .use { assert(it.body!!.string() == "Are you just going to ask me like that? Ծ‸Ծ") }
    }

    @Test
    fun testWithCorrectHeader()
    {
        val request = Builder().post(body).header("cute", "yes").url("http://localhost:1029/api/echo").build()
        http.newCall(request).execute()
            .use { assert(it.body!!.string() == "Thank you! (⺣◡⺣)\nI'm a cat") }
    }


    @Test
    @Throws(IOException::class)
    fun testJson()
    {
        val request = Builder()
            .post(Model(23.0, 33.0).toString().toRequestBody("application/json".toMediaType()))
            .url("http://localhost:1029/json/divide").build()
        http.newCall(request).execute()
            .use { assert(floor(it.body!!.string().toDouble() * 100) == 69.0) }
    }

    @Test
    @Throws(IOException::class)
    fun testJsonWithoutBody()
    {
        val request = Builder().url("http://localhost:1029/json/divide").build()
        http.newCall(request).execute()
            .use { assert(it.body!!.string() == "{\"error\":\"Request body is empty.\"}") }
    }

    @Test
    @Throws(IOException::class)
    fun testJsonWithIncorrectModel()
    {
        val request = Builder()
            .post("{\"name\": \"Yukari Yakumo\", \"age\": \"At least 1200anghhkjlhjhgfsdfhjklfc\"}"
                .toRequestBody("application/json".toMediaType()))
            .url("http://localhost:1029/json/divide").build()
        http.newCall(request).execute()
            .use { assert(it.body!!.string().startsWith("{\"error\":\"Error during json parsing: ")) }
    }

    @Test
    @Throws(IOException::class)
    fun testJsonWithUnparseableModel()
    {
        val request = Builder()
            .post("The quick brown fox jumps over the lazy dog.".toRequestBody("application/json".toMediaType()))
            .url("http://localhost:1029/json/divide").build()
        http.newCall(request).execute()
            .use { assert(it.body!!.string().startsWith("{\"error\":\"Error during json parsing: ")) }
    }

    @Test
    @Throws(IOException::class)
    fun testJsonWithDataTooLong()
    {
        val request = Builder()
            .post("The quick brown fox didn't jump over the lazy dog, but decided to slide under the lazy dog this time."
                .toRequestBody("application/json".toMediaType()))
            .url("http://localhost:1029/json/divide").build()
        http.newCall(request).execute()
            .use { assert(it.body!!.string() == "{\"error\":\"Body too long. (101/80)\"}") }
    }

    @Test
    @Throws(IOException::class)
    fun testJsonWithInternalError()
    {
        val request = Builder()
            .post(Model(1337.0, 0.0).toString().toRequestBody("application/json".toMediaType()))
            .url("http://localhost:1029/json/divide").build()
        http.newCall(request).execute().use { assert(it.body!!.string() == "Infinity") }
    }
}
