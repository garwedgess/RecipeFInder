package eu.wedgess.recipefinder.data.api

import eu.wedgess.recipefinder.data.model.AvailableIngredientsData
import eu.wedgess.recipefinder.data.model.IngredientsApiResponseData

interface IngredientsApi {

    suspend fun getAvailableIngredients(): IngredientsApiResponseData<AvailableIngredientsData>

    companion object {
        const val BASE_URL = "https://run.mocky.io/v3"
    }

}