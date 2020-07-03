package org.hydev

import org.eclipse.jetty.server.Request
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * An ApiAccess instance contains the data that the client sent to the server.
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-03 12:37
 */
class ApiAccess(
    target: String,
    baseRequest: Request,
    request: HttpServletRequest,
    response: HttpServletResponse,
    val body: String
): HttpAccess(target, baseRequest, request, response)
{
    val headers = request.mapHeaders()
}
