package eu.wedgess.recipefinder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.recipefinder.domain.RecipeIngredientsRepository
import eu.wedgess.recipefinder.domain.usecases.GetIngredientsAndRecipesUseCase
import eu.wedgess.recipefinder.domain.usecases.RemoveIngredientUseCase
import javax.inject.Singleton

/**
 * Hilt - Use case module for injecting the use cases into the ViewModel
 *
 */
@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetIngredientsAndRecipesUseCase(recipeIngredientsRepository: RecipeIngredientsRepository): GetIngredientsAndRecipesUseCase {
        return GetIngredientsAndRecipesUseCase(recipeIngredientsRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveIngredientUseCase(recipeIngredientsRepository: RecipeIngredientsRepository): RemoveIngredientUseCase {
        return RemoveIngredientUseCase(recipeIngredientsRepository)
    }
}