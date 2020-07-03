package org.hydev

import org.eclipse.jetty.server.Server
import java.io.IOException
import java.lang.System.err
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.*

/**
 * TODO: Write a description for this class!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-03 12:31
 */
class ApiServer(
    val port: Int
)
{
    // Registered nodes
    val nodes = ApiNodeManager()

    // Jetty handler
    val jettyHandler = JettyHandler(this)

    // Jetty server
    val jetty = Server(port).apply { handler = jettyHandler }

    // Error handlers
    var handleNullRequest: () -> Unit = { err.println("Error: Somehow a Jetty parameter is null when handle is called") }
    var handleWrongMethod: (HttpAccess) -> Unit = { it.write(SC_BAD_REQUEST, "{\"error\": \"Error: invalid method\"}") }
    var handleEmptyPath: (HttpAccess) -> Unit = { it.write(SC_BAD_REQUEST, "What?") }
    var handleNodeNotFound: (HttpAccess) -> Unit = { it.write(SC_NOT_FOUND, "Not found.") }

    // Accepted methods
    var acceptedMethods = mutableListOf("get", "post")

    // Configure default response
    var configureResponse: (HttpServletResponse) -> Unit = {
        it.status = SC_OK
        it.contentType = "application/json; charset=utf-8"
        it.setHeader("Access-Control-Allow-Origin", "*")
        it.setHeader("Access-Control-Allow-Credentials", "true")
    }
}
