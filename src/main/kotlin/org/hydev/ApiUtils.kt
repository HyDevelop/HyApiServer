package org.hydev

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Convert headers from
 */
fun HttpServletRequest.mapHeaders(): Map<String, String>
{
    val headers: MutableMap<String, String> = HashMap()
    val keysEnumeration = headerNames
    while (keysEnumeration.hasMoreElements())
    {
        val key = keysEnumeration.nextElement()
        headers[key] = getHeader(key)
    }
    return headers
}

/**
 * Write response
 *
 * @param text Response text
 * @return Success or not
 */
fun HttpServletResponse.write(text: String): Boolean
{
    return try
    {
        writer.print(text)
        true
    }
    catch (e: IOException)
    {
        e.printStackTrace()
        false
    }
}

val json = Json(JsonConfiguration.Stable)

/**
 * Convert text to error json format
 *
 * @param text Error message
 */
fun jsonError(text: String?) = json.stringify(JsonError.serializer(), JsonError(text))

@Serializable
private data class JsonError(val error: String?)
