package org.hydev

/**
 * Api node manager
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-03 12:34
 */
class ApiNodeManager
{
    private val nodes = ArrayList<ApiNode>()

    /**
     * Register one or many nodes
     *
     * @param nodes API Nodes that you want to register.
     */
    fun register(vararg nodes: ApiNode)
    {
        this.nodes.addAll(nodes)
    }

    /**
     * Fancier way to register a node
     *
     * @param node
     */
    operator fun plusAssign(node: ApiNode)
    {
        register(node)
    }

    /**
     * Get api node
     *
     * @param path Path of the node
     * @return Api node or null
     */
    operator fun get(path: String): ApiNode?
    {
        val lowerPath = path.toLowerCase()

        for (node in nodes)
        {
            if (node.path == lowerPath)
            {
                return node
            }
        }

        return null
    }
}
