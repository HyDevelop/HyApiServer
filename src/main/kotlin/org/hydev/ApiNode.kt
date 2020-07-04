package org.hydev

/**
 * Each api node represents a specific location on the server.
 * (Eg. "http://.../hello/world" has the path "/hello/world")
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-03 12:34
 */
abstract class ApiNode(path: String, val isSecret: Boolean = false)
{
    val path = path.toLowerCase()

    abstract fun process(access: ApiAccess): Any?
}
