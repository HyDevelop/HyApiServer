package org.hydev

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.IOException


/**
 * Convert headers from
 */
fun HttpServletRequest.mapHeaders(): Map<String, String> {
    val headers: MutableMap<String, String> = HashMap()
    val keysEnumeration = headerNames
    while (keysEnumeration.hasMoreElements()) {
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
fun HttpServletResponse.write(text: String): Boolean {
    return try {
        writer.print(text)
        true
    } catch (e: IOException) {
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

/**
 * Determine whether some object is a "primitive" type considered by Json.
 *
 * @return Primitive or not
 */
fun Any.isPrimitive() = this is String || this is Boolean || this is Int || this is Long
        || this is Double || this is Float || this is Char || this is Byte || this is Short
