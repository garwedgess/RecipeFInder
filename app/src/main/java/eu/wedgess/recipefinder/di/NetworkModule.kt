package eu.wedgess.recipefinder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.recipefinder.data.api.IngredientsApi
import eu.wedgess.recipefinder.data.api.IngredientsApiImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

/**
 * Hilt - Network module for injecting the HTTP client into the [IngredientsApi] and
 * injecting the [IngredientsApi] into the [IngredientsRemoteDataSource]
 *
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            // custom logger set to use Timber
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
            }
            engine {
                requestTimeout = 10_000
            }
            // Throw exceptions based on response codes, these will later be wrapped by [ApiResponse]
            HttpResponseValidator {
                validateResponse { response: HttpResponse ->
                    val statusCode = response.status.value

                    when (statusCode) {
                        in 300..399 -> throw RedirectResponseException(
                            response,
                            "Code: $statusCode"
                        )
                        in 400..499 -> throw ClientRequestException(
                            response,
                            "Code: $statusCode"
                        )
                        in 500..599 -> throw ServerResponseException(
                            response,
                            "Code: $statusCode"
                        )
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

    @Provides
    @Singleton
    fun provideApi(client: HttpClient): IngredientsApi {
        return IngredientsApiImpl(client)
    }
}