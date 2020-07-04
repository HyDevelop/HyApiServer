package org.hydev;

import org.jetbrains.annotations.NotNull;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

/**
 * An example of an api node written in Java.
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-03 17:37
 */
public class JavaExampleNode extends ApiNode
{
    public JavaExampleNode()
    {
        super("/api/echo", false);
    }

    @Override
    public Object process(@NotNull ApiAccess access)
    {
        // Check body
        if (access.getBody().isEmpty())
        {
            // Write regular responses by returning the text:
            return "What do you want me to say?";
        }

        // Check headers
        if (!access.getHeaders().containsKey("cute"))
        {
            return "Are you just going to ask me like that? Ծ‸Ծ";
        }

        if (access.getHeaders().get("cute").equalsIgnoreCase("no"))
        {
            // Write custom responses like this:
            access.getResponse().setContentType("text/plain");
            access.write(SC_FORBIDDEN, "Really? Then I'm not saying it! <(ˉ^ˉ)>");
            return null;
        }

        if (access.getHeaders().get("cute").equalsIgnoreCase("yes"))
        {
            return "Thank you! (⺣◡⺣)\n" + access.getBody();
        }

        return "What?";
    }
}
