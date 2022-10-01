package eu.wedgess.recipefinder.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.recipefinder.data.repository.RecipeIngredientsRepositoryImpl
import eu.wedgess.recipefinder.domain.RecipeIngredientsRepository

/**
 * Hilt - Repository module for injecting the [RecipeIngredientsRepository] into the use cases
 *
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindIngredientsRepository(recipeIngredientsRepositoryImpl: RecipeIngredientsRepositoryImpl): RecipeIngredientsRepository

}