package eu.wedgess.recipefinder.data.api

import eu.wedgess.recipefinder.data.api.IngredientsApi
import eu.wedgess.recipefinder.data.model.ApiResponse
import eu.wedgess.recipefinder.data.model.AvailableIngredientsData
import eu.wedgess.recipefinder.data.utils.safeRequest
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs


/**
 * This class test that the correct [ApiResponse] and data is returned from the API endpoint.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class IngredientsApiImplTest {


    /**
     * Given - the correct response
     * When - the request is made
     * Then - the response is [ApiResponse.Success]
     */
    @Test
    fun givenCorrectDataThenResponseIsSuccess() = runTest {
        // Given
        val successData = """["Meat", "egg", "onion", "sugar"]"""
        // When
        val response =
            createMockClient(successData).safeRequest<AvailableIngredientsData, String> {
                method = HttpMethod.Get
                url("${IngredientsApi.BASE_URL}/45a5a07f-e981-4918-9c31-090b121d6c5f")
            }
        // Then
        assertIs<ApiResponse.Success<AvailableIngredientsData>>(response)
    }


    /**
     * Given - the correct data
     * When - the request is made
     * Then - the response contains the correct data
     */
    @Test
    fun givenCorrectDataThenResponseContainsCorrectData() = runTest {
        // Given
        val successData = """["Meat", "egg", "onion", "sugar"]"""
        // When
        val response =
            createMockClient(successData).safeRequest<AvailableIngredientsData, String> {
                method = HttpMethod.Get
                url("${IngredientsApi.BASE_URL}/45a5a07f-e981-4918-9c31-090b121d6c5f")
            }
        // Then
        assertContentEquals(
            listOf("Meat", "egg", "onion", "sugar"),
            (response as ApiResponse.Success).body.ingredients
        )
    }


    /**
     * Given - Badly formatted json data
     * When - The request is made
     * Then - response is [ApiResponse.Error.SerializationError]
     */
    @Test
    fun givenBadlyFormattedJsonThenResponseIsSerializationError() = runTest {
        // Given
        val badlyFormattedJsonData = """["Meat", "egg", "onion", "sugar]"""
        // When
        val response =
            createMockClient(badlyFormattedJsonData).safeRequest<AvailableIngredientsData, String> {
                method = HttpMethod.Get
                url("${IngredientsApi.BASE_URL}/45a5a07f-e981-4918-9c31-090b121d6c5f")
            }
        // Then
        assertIs<ApiResponse.Error.SerializationError>(response)
    }

    /**
     * Given - Http response status code of 404
     * When - The request is made
     * Then - response is [ApiResponse.Error.HttpError]
     */
    @Test
    fun givenUnknownEndpointThenResponseIsHttpError() = runTest {
        // Given
        val statusCode = HttpStatusCode.NotFound
        // When
        val response =
            createMockClient(
                responseData = "Not found",
                responseStatusCode = statusCode
            ).safeRequest<String, String> {
                method = HttpMethod.Get
                url("${IngredientsApi.BASE_URL}/%20error%20response")
            }
        // Then
        assertIs<ApiResponse.Error.HttpError<String>>(response)
    }

    /**
     * Given - Http response status code of 500
     * When - The request is made
     * Then - response is [ApiResponse.Error.HttpError]
     */
    @Test
    fun givenServerErrorThenResponseIsHttpError() = runTest {
        // Given
        val statusCode = HttpStatusCode.InternalServerError
        // When
        val response =
            createMockClient(
                responseData = "Unknown error",
                responseStatusCode = statusCode
            ).safeRequest<String, String> {
                method = HttpMethod.Get
                url("${IngredientsApi.BASE_URL}/45a5a07f-e981-4918-9c31-090b121d6c5f")
            }
        // Then
        assertIs<ApiResponse.Error.HttpError<String>>(response)
    }

    /**
     * Given - Http response status code of 500
     * When - The request is made
     * Then - response error message is [ApiResponse.Error.HttpError]
     */
    @Test
    fun givenServerErrorThenResponseErrorMessageIsCorrect() = runTest {
        // Given
        val responseErrorMessage = "Unknown error"
        // When
        val response =
            createMockClient(
                responseData = responseErrorMessage,
                responseStatusCode = HttpStatusCode.InternalServerError
            ).safeRequest<String, String> {
                method = HttpMethod.Get
                url("${IngredientsApi.BASE_URL}/45a5a07f-e981-4918-9c31-090b121d6c5f")
            }
        // Then
        assertEquals(responseErrorMessage, (response as ApiResponse.Error.HttpError).errorBody)
    }


    /**
     * Create mock HttpClient for testing
     *
     * @param responseData - response data to be returned by client
     * @param responseStatusCode - status code to test HttpError
     * @return our mocked HttpClient
     */
    private fun createMockClient(
        responseData: String,
        responseStatusCode: HttpStatusCode = HttpStatusCode.OK
    ): HttpClient {
        val mockEngine = MockEngine {
            if (responseStatusCode == HttpStatusCode.OK) {
                respond(
                    content = ByteReadChannel(responseData),
                    status = responseStatusCode,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                )
            } else {
                respondError(
                    content = responseData,
                    status = responseStatusCode,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            }
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            HttpResponseValidator {
                validateResponse { response: HttpResponse ->
                    val statusCode = response.status.value

                    println("HTTP status: $statusCode")

                    when (statusCode) {
                        in 300..399 -> throw RedirectResponseException(
                            response,
                            "Code: $statusCode"
                        )
                        in 400..499 -> throw ClientRequestException(response, "Code: $statusCode")
                        in 500..599 -> throw ServerResponseException(response, "Code: $statusCode")
                    }

                    if (statusCode >= 600) {
                        throw ResponseException(response, "Code: $statusCode")
                    }
                }
                handleResponseExceptionWithRequest { exception, _ ->
                    throw exception
                }
            }
        }
    }
}