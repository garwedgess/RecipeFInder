package eu.wedgess.recipefinder.domain.usecases

import eu.wedgess.recipefinder.domain.RecipeIngredientsRepository
import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.Resource
import javax.inject.Inject

class RemoveIngredientUseCase @Inject constructor(private val recipeIngredientsRepository: RecipeIngredientsRepository) :
    UseCase<Resource<IngredientsWithRecipesEntity>, String> {

    override suspend fun execute(ingredient: String): Resource<IngredientsWithRecipesEntity> {
        return recipeIngredientsRepository.removeIngredient(ingredient)
    }
}