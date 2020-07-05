package org.hydev

import kotlinx.serialization.Serializable

/**
 * TODO: Write a description for this class!
 *
 * @author HyDEV Team (https://github.com/HyDevelop)
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @author Vanilla (https://github.com/VergeDX)
 * @since 2020-07-04 11:16
 */
class KotlinExampleJsonNode: JsonApiNode<KotlinExampleJsonNode.Model>("/json/divide", Model::class, maxLength = 80)
{
    /**
     * Model of the data passed in through request body.
     */
    @Serializable
    data class Model(val dividend: Double, val divisor: Double)
    {
        override fun toString() = json.stringify(serializer(), this)
    }

    /**
     * Process json data which is already automatically parsed.
     * In this case, this api does a simple division of the two numbers given.
     *
     * Note: If you return an object of a class, it would be automatically
     * stringified as an Json object, but if you return a String or a primitive,
     * it would be returned directly.
     */
    override fun json(access: ApiAccess, data: Model) = data.dividend / data.divisor
}
