package eu.wedgess.recipefinder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.recipefinder.data.api.IngredientsApi
import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsCacheDataSource
import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsRemoteDataSource
import eu.wedgess.recipefinder.data.datasource.recipes.RecipesCacheDataSource
import eu.wedgess.recipefinder.data.mappers.AvailableIngredientResponseMapper
import eu.wedgess.recipefinder.data.mappers.RecipeMapper
import javax.inject.Singleton

/**
 * Hilt - Data source module for injecting the data sources into the repository
 */
@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideIngredientsCacheDataSource(): IngredientsCacheDataSource {
        return IngredientsCacheDataSource()
    }

    @Provides
    @Singleton
    fun provideIngredientsRemoteDataSource(
        api: IngredientsApi,
        mapper: AvailableIngredientResponseMapper
    ): IngredientsRemoteDataSource {
        return IngredientsRemoteDataSource(api, mapper)
    }

    @Provides
    @Singleton
    fun provideRecipesCacheDataSource(recipeMapper: RecipeMapper): RecipesCacheDataSource {
        return RecipesCacheDataSource(recipeMapper)
    }
}