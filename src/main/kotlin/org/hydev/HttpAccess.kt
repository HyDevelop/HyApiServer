package org.hydev

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Request

/**
 * HTTP Access before api information is obtained
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-03 16:14
 */
open class HttpAccess(
    val path: String,
    val baseRequest: Request,
    val request: HttpServletRequest,
    val response: HttpServletResponse
) {
    fun write(text: String) = response.write(text)
    fun write(sc: Int, text: String) = response.apply { status = sc }.write(text)
}
