package org.hydev

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Jetty handler that processes raw http requests and maps them to api nodes.
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-03 12:32
 */
class JettyHandler(private val server: ApiServer) : AbstractHandler()
{
    /**
     * Handle requests and map them to either api nodes or error handlers.
     */
    override fun handle(path: String?, baseRequest: Request?, request: HttpServletRequest?, response: HttpServletResponse?)
    {
        // Request is null (This shouldn't happen)
        if (path == null || baseRequest == null || request == null || response == null)
        {
            server.handleNullRequest()
            return
        }

        // Set handled so that Jetty doesn't ask the next handler.
        baseRequest.isHandled = true

        // Configure default response
        server.configureResponse(response)

        // Limit request methods
        if (!server.acceptedMethods.contains(request.method.toLowerCase()))
        {
            server.handleWrongMethod(HttpAccess(path, baseRequest, request, response))
            return
        }

        // Get node
        val node: ApiNode? = server.nodes[path]

        // Node not found
        if (node == null)
        {
            server.handleNodeNotFound(HttpAccess(path, baseRequest, request, response))
            return
        }

        // Get body
        val content = request.reader.lines().collect(Collectors.joining(System.lineSeparator()))
        val access = ApiAccess(path, baseRequest, request, response, node, content)

        try
        {
            // Process api, and write response based on the result of api processing
            when (val result = node.process(access))
            {
                null, is Unit -> response.write("")
                else -> response.write(result.toString())
            }
        }
        catch (e: Exception)
        {
            // Handle general exception while processing
            if (server.isSuppressed(e)) server.handleSuppressedError(access, e)
            else server.handleError(access, e)
        }
    }
}
