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


    // Accepted methods
    var acceptedMethods = mutableListOf("get", "post")
}
