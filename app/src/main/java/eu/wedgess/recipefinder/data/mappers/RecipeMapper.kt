package eu.wedgess.recipefinder.data.mappers

import eu.wedgess.recipefinder.data.model.RecipeData
import eu.wedgess.recipefinder.domain.Mapper
import eu.wedgess.recipefinder.domain.entities.RecipeEntity

/**
 * Recipe mapper to map [RecipeData] to [RecipeEntity] in order to not violate clean architecture.
 *
 * @constructor Create empty Recipe mapper
 */
class RecipeMapper : Mapper<RecipeEntity, RecipeData> {

    override fun mapFromEntity(entity: RecipeEntity): RecipeData {
        return RecipeData(
            name = entity.name,
            ingredients = entity.ingredients
        )
    }

    override fun mapToEntity(data: RecipeData): RecipeEntity {
        return RecipeEntity(
            name = data.name,
            ingredients = data.ingredients
        )
    }
}