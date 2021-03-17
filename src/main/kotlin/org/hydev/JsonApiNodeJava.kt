package org.hydev

/**
 * JsonApiNode that is compatible with Java
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-04 10:24
 */
abstract class JsonApiNodeJava<T>(
    path: String,
    val modelClass: Class<T>,
    isSecret: Boolean,
    val maxLength: Int
) : ApiNode(path) {
    /**
     * This is the parser interface to support other java json parsing
     * libraries. (Since this project is focused mainly on Kotlin, I'm
     * not going to add GSON as a default dependency.)
     */
    interface JavaJsonParser {
        fun <T> parse(type: Class<T>, json: String): T
        fun <T> stringify(type: Class<T>, data: Any): String
    }

    companion object {
        @JvmStatic
        lateinit var parser: JavaJsonParser
    }

    /**
     * Process http node access to check for stuff and parse json.
     *
     * @param access Http node access
     * @return Data sent back to the user
     */
    override fun process(access: ApiAccess): Any? {
        // Validate body
        if (access.body.isEmpty()) return jsonError("Request body is empty.")
        if (access.body.length > maxLength) {
            throw KnownException("Body too long. (${access.body.length}/$maxLength)")
        }

        // Parse body as json
        val data: T
        try {
            data = parser.parse(modelClass, access.body)
        } catch (e: Exception) {
            var msg = "Error during json parsing: ${e.message}"
            if (!isSecret) msg += " for ${access.body}"
            throw KnownException(msg)
        }

        // Get and return processed results
        val result = json(access, data)
        return if (result == null || result.isPrimitive()) result else parser.stringify(modelClass, result)
    }

    abstract fun json(access: ApiAccess, data: T): Any?
}
