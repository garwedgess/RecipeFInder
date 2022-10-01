package eu.wedgess.recipefinder.domain.usecases

interface UseCase<Type, ParamsType> {

    suspend fun execute(params: ParamsType): Type
}
