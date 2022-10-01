package eu.wedgess.recipefinder.domain.usecases

import eu.wedgess.recipefinder.domain.RecipeIngredientsRepository
import eu.wedgess.recipefinder.domain.entities.IngredientsWithRecipesEntity
import eu.wedgess.recipefinder.domain.entities.Resource
import javax.inject.Inject

class GetIngredientsAndRecipesUseCase @Inject constructor(private val recipeIngredientsRepository: RecipeIngredientsRepository) :
    UseCase<Resource<IngredientsWithRecipesEntity>, Boolean> {

    override suspend fun execute(refresh: Boolean): Resource<IngredientsWithRecipesEntity> {
        return recipeIngredientsRepository.getAvailableIngredients(refresh)
    }
}