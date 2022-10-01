package eu.wedgess.recipefinder.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

/**
 * Data class to represent the list of available ingredients
 */
@Serializable(AvailableIngredientsDataSerializer::class)
data class AvailableIngredientsData(
    val ingredients: List<String>
)

/**
 * Custom serializer used by [AvailableIngredientsData] to parse the JSON array
 */
object AvailableIngredientsDataSerializer : KSerializer<AvailableIngredientsData> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor(AvailableIngredientsData::class.simpleName!!) {
            element<List<String>>(AvailableIngredientsData::ingredients.name)
        }

    override fun serialize(encoder: Encoder, value: AvailableIngredientsData) {
        error("Serialization is not supported")
    }

    override fun deserialize(decoder: Decoder): AvailableIngredientsData {
        require(decoder is JsonDecoder)
        val jsonArray = decoder.decodeJsonElement().jsonArray
        return AvailableIngredientsData(
            ingredients = jsonArray.map { value -> value.jsonPrimitive.content }
        )
    }
}
