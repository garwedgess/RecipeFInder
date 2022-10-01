package eu.wedgess.recipefinder.domain

/**
 * Interface for model mappers. It provides helper methods that facilitate
 * retrieving of models from outer data source layers
 */
interface Mapper<E, D> {

    fun mapFromEntity(entity: E): D

    fun mapToEntity(data: D): E

}
