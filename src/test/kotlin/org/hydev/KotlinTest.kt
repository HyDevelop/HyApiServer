package org.hydev

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request.Builder
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KotlinTest
{
    private val server: ApiServer = ApiServer(1029)
    private val http = OkHttpClient()
    private val body = "I'm a cat".toRequestBody("text/plain".toMediaType());

    @BeforeAll
    fun init()
    {
        server.nodes.register(KotlinExampleNode())
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
            assert(response.body!!.string() == "Not found.")
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
}
