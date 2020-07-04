package org.hydev

import javax.servlet.http.HttpServletResponse.SC_FORBIDDEN

/**
 * An example of an api node written in Java.
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-03 17:37
 */
class KotlinExampleNode : ApiNode("/api/echo")
{
    override fun process(access: ApiAccess): Any?
    {
        // Write regular responses by returning the text:
        return if (access.body.isEmpty()) "What do you want me to say?"
        else if (!access.headers.containsKey("cute")) "Are you just going to ask me like that? Ծ‸Ծ"
        else if (access.headers["cute"]!!.toLowerCase() == "no")
        {
            // Write custom responses using access.response and calling access.write:
            access.response.contentType = "text/plain"
            access.write(SC_FORBIDDEN, "Really? Then I'm not saying it! <(ˉ^ˉ)>")

            // Return null if you don't want to write anything else.
            null
        }
        else if (access.headers["cute"]!!.toLowerCase() == "yes") "Thank you! (⺣◡⺣)\n${access.body}"
        else "What?"
    }
}
