package org.hydev

import javax.servlet.http.HttpServletRequest

/**
 * Convert headers from
 */
fun HttpServletRequest.mapHeaders(): Map<String, String>?
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
