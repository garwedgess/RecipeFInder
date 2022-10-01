package eu.wedgess.recipefinder.data.mapper

import eu.wedgess.recipefinder.data.mappers.AvailableIngredientResponseMapper
import eu.wedgess.recipefinder.data.model.ApiResponse
import eu.wedgess.recipefinder.data.model.AvailableIngredientsData
import eu.wedgess.recipefinder.domain.entities.Resource
import kotlinx.serialization.SerializationException
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals

/**
 * This class tests that [ApiResponse] is mapped to the correct [Resource]
 *
 */
class AvailableIngredientResponseMapperTest {

    private lateinit var mapper: AvailableIngredientResponseMapper

    @Before
    fun setup() {
        mapper = AvailableIngredientResponseMapper()
    }

    /**
     * Given - [ApiResponse.Error.NetworkError]
     * When - [AvailableIngredientResponseMapper.mapToEntity] is called
     * Then - [ApiResponse.Error.NetworkError] is mapped to [Resource.Error]
     */
    @Test
    fun apiResponseNetworkErrorMapsToResourceError() {
        val errorMsg = "error"
        val data = ApiResponse.Error.NetworkError(IOException(errorMsg))
        assertEquals(Resource.Error(errorMsg), mapper.mapToEntity(data))
    }

    /**
     * Given - [ApiResponse.Error.SerializationError]
     * When - [AvailableIngredientResponseMapper.mapToEntity] is called
     * Then - [ApiResponse.Error.NetworkError] is mapped to [Resource.Error]
     */
    @Test
    fun apiResponseSerializationErrorMapsToResourceError() {
        val errorMsg = "error"
        val data = ApiResponse.Error.SerializationError(SerializationException(errorMsg))
        assertEquals(Resource.Error(errorMsg), mapper.mapToEntity(data))
    }

    /**
     * Given - [ApiResponse.Error.HttpError]
     * When - [AvailableIngredientResponseMapper.mapToEntity] is called
     * Then - [ApiResponse.Error.NetworkError] is mapped to [Resource.Error]
     */
    @Test
    fun apiResponseHttpErrorMapsToResourceError() {
        val errorMsg = "Internal server error"
        val data = ApiResponse.Error.HttpError(code = 501, errorBody = errorMsg)
        assertEquals(Resource.Error(errorMsg), mapper.mapToEntity(data))
    }

    /**
     * Given - [ApiResponse.Success] with empty data
     * When - [AvailableIngredientResponseMapper.mapToEntity] is called
     * Then - [ApiResponse.Success] is mapped to [Resource.Empty]
     */
    @Test
    fun apiResponseSuccessWithEmptyListMapsToResourceEmpty() {
        val data = ApiResponse.Success(AvailableIngredientsData(ingredients = emptyList()))
        assertEquals(Resource.Empty, mapper.mapToEntity(data))
    }

    /**
     * Given - [ApiResponse.Success] with data
     * When - [AvailableIngredientResponseMapper.mapToEntity] is called
     * Then - [ApiResponse.Success] is mapped to [Resource.Success]
     */
    @Test
    fun apiResponseSuccessWithDataMapsToResourceSuccess() {
        val ingredientsData = listOf("egg", "Meat")
        val data = ApiResponse.Success(AvailableIngredientsData(ingredients = ingredientsData))
        assertEquals(Resource.Success(ingredientsData), mapper.mapToEntity(data))
    }

    /**
     * Given - [Resource]
     * When - mapping [Resource] to [ApiResponse]
     * Then - [UnsupportedOperationException] is thrown
     */
    @Test(expected = UnsupportedOperationException::class)
    fun mappingResourceToApiResponseThrowsException() {
        mapper.mapFromEntity(Resource.Empty)
    }

}