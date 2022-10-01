package eu.wedgess.recipefinder.data.api

import eu.wedgess.recipefinder.data.model.AvailableIngredientsData
import eu.wedgess.recipefinder.data.model.IngredientsApiResponseData
import eu.wedgess.recipefinder.data.utils.safeRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * The Ingredients API implementation
 *
 * @property client - Ktor [HttpClient]
 * @constructor Injects our [client] using dependency inection
 */
@Singleton
class IngredientsApiImpl @Inject constructor(private val client: HttpClient) : IngredientsApi {


    /**
     * Get available ingredients from the REST API
     *
     * @return - [IngredientsApiResponseData] the caller must handle the different [eu.wedgess.recipefinder.data.model.ApiResponse] types
     */
    override suspend fun getAvailableIngredients(): IngredientsApiResponseData<AvailableIngredientsData> =
        withContext(Dispatchers.IO) {
            client.safeRequest {
                method = HttpMethod.Get
                url("${IngredientsApi.BASE_URL}/45a5a07f-e981-4918-9c31-090b121d6c5f")
            }
        }
}