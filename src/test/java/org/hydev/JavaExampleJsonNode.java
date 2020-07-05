package org.hydev;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

/**
 * JsonApiNode that is compatible with Java
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-04 10:24
 */
public class JavaExampleJsonNode extends JsonApiNodeJava<JavaExampleJsonNode.Model>
{
    /*
     * Create parser (You only have to do this only once.
     * I recommend putting this into the main function, but I put it here for
     * the clarity of this demonstration.)
     */
    static
    {
        Gson gson = new Gson();
        JsonApiNodeJava.parser = new JavaJsonParser()
        {
            @Override
            public <T> T parse(@NotNull Class<T> type, @NotNull String json)
            {
                return gson.fromJson(json, type);
            }

            @NotNull
            @Override
            public <T> String stringify(@NotNull Class<T> type, @NotNull Object data)
            {
                return gson.toJson(data);
            }
        };
    }

    /**
     * Create node
     */
    public JavaExampleJsonNode()
    {
        // (Path, Model class, Is secret, Max length).
        super("/json/divide", Model.class, false, 80);
    }

    /**
     * Model of the data passed in through request body.
     */
    static class Model
    {
        double dividend;
        double divisor;
    }

    /**
     * Process json data which is already automatically parsed.
     * In this case, this api does a simple division of the two numbers given.
     *
     * Note: If you return an object of a class, it would be automatically
     * stringified as an Json object, but if you return a String or a primitive,
     * it would be returned directly.
     */
    @Override
    public Object json(@NotNull ApiAccess access, Model data)
    {
        return data.dividend / data.divisor;
    }
}
