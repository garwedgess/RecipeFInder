package eu.wedgess.recipefinder.data.datasource

import eu.wedgess.recipefinder.data.api.IngredientsApi
import eu.wedgess.recipefinder.data.datasource.ingredients.IngredientsRemoteDataSource
import eu.wedgess.recipefinder.data.mappers.AvailableIngredientResponseMapper
import eu.wedgess.recipefinder.data.model.ApiResponse
import eu.wedgess.recipefinder.data.model.AvailableIngredientsData
import eu.wedgess.recipefinder.domain.entities.Resource
import io.ktor.http.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertIs


/**
 * This class test the correct [Resource] and data is returned from the [IngredientsApi]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class IngredientsRemoteDataSourceTest {

    private lateinit var ingredientsRemoteDataSource: IngredientsRemoteDataSource

    @MockK
    private lateinit var ingredientsApi: IngredientsApi

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        ingredientsRemoteDataSource =
            IngredientsRemoteDataSource(ingredientsApi, AvailableIngredientResponseMapper())
    }

    @After
    fun teardown() = unmockkAll()


    /**
     * Given - [IngredientsApi.getAvailableIngredients] returns [ApiResponse.Success] with [List<String>] data
     * When - [IngredientsRemoteDataSource.getAvailableIngredients] is called
     * Then - [IngredientsApi.getAvailableIngredients] is called exactly once
     */
    @Test
    fun repositoryGetIngredientsCallsApiGetIngredientsExactlyOnce() {
        coEvery { ingredientsApi.getAvailableIngredients() } returns ApiResponse.Success(
            AvailableIngredientsData(ingredients = listOf("Meat", "onion"))
        )
        runTest {
            ingredientsRemoteDataSource.getAvailableIngredients()
        }
        coVerify(exactly = 1) { ingredientsApi.getAvailableIngredients() }
    }

    /**
     * Given - [IngredientsApi.getAvailableIngredients] returns [ApiResponse.Success] with empty data
     * When - [IngredientsRemoteDataSource.getAvailableIngredients] is called
     * Then - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Empty]
     */
    @Test
    fun repositoryGetIngredientsReturnsEmptyWhenThereIsNoData() {
        coEvery { ingredientsApi.getAvailableIngredients() } returns ApiResponse.Success(
            AvailableIngredientsData(ingredients = emptyList())
        )
        runTest {
            val response = ingredientsRemoteDataSource.getAvailableIngredients()
            assertIs<Resource.Empty>(response)
        }
    }

    /**
     * Given - [IngredientsApi.getAvailableIngredients] returns [ApiResponse.Success] with [List<String>] data
     * When - [IngredientsRemoteDataSource.getAvailableIngredients] is called
     * Then - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Success]
     */
    @Test
    fun repositoryGetIngredientsReturnsSuccessResponse() {
        coEvery { ingredientsApi.getAvailableIngredients() } returns ApiResponse.Success(
            AvailableIngredientsData(ingredients = listOf("Meat", "onion", "cheese"))
        )
        runTest {
            val response = ingredientsRemoteDataSource.getAvailableIngredients()
            assertIs<Resource.Success<AvailableIngredientsData>>(response)
        }
    }

    /**
     * Given - [IngredientsApi.getAvailableIngredients] returns [ApiResponse.Success] with [List<String>] data
     * When - [IngredientsRemoteDataSource.getAvailableIngredients] is called
     * Then - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Success] with correct data
     */
    @Test
    fun repositoryGetIngredientsReturnsCorrectDataOnSuccessResponse() {
        val responseData = listOf("Meat", "onion", "cheese", "egg")
        coEvery { ingredientsApi.getAvailableIngredients() } returns ApiResponse.Success(
            AvailableIngredientsData(ingredients = responseData)
        )
        runTest {
            val response = ingredientsRemoteDataSource.getAvailableIngredients()
            assertEquals(
                responseData,
                (response as Resource.Success<List<String>>).data
            )
        }
    }

    /**
     * Given - [IngredientsApi.getAvailableIngredients] returns [ApiResponse.Error.HttpError]
     * When - [IngredientsRemoteDataSource.getAvailableIngredients] is called
     * Then - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Error]
     */
    @Test
    fun repositoryGetIngredientsReturnsHttpErrorResponse() {
        coEvery { ingredientsApi.getAvailableIngredients() } returns ApiResponse.Error.HttpError(
            code = HttpStatusCode.InternalServerError.value, errorBody = "Unknown error"
        )
        runTest {
            val response = ingredientsRemoteDataSource.getAvailableIngredients()
            assertIs<Resource.Error>(response)
        }
    }

    /**
     * Given - [IngredientsApi.getAvailableIngredients] returns [ApiResponse.Error.SerializationError]
     * When - [IngredientsRemoteDataSource.getAvailableIngredients] is called
     * Then - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Error]
     */
    @Test
    fun repositoryGetIngredientsReturnsSerializationErrorResponse() {
        coEvery { ingredientsApi.getAvailableIngredients() } returns ApiResponse.Error.SerializationError(
            SerializationException("error message")
        )
        runTest {
            val response = ingredientsRemoteDataSource.getAvailableIngredients()
            assertIs<Resource.Error>(response)
        }
    }

    /**
     * Given - [IngredientsApi.getAvailableIngredients] returns [ApiResponse.Error.NetworkError]
     * When - [IngredientsRemoteDataSource.getAvailableIngredients] is called
     * Then - [IngredientsRemoteDataSource.getAvailableIngredients] returns [Resource.Error]
     */
    @Test
    fun repositoryGetIngredientsReturnsNetworkErrorResponse() {
        coEvery { ingredientsApi.getAvailableIngredients() } returns ApiResponse.Error.NetworkError(
            IOException("error message")
        )
        runTest {
            val response = ingredientsRemoteDataSource.getAvailableIngredients()
            assertIs<Resource.Error>(response)
        }
    }

}