package eu.wedgess.recipefinder.data.mapper

import eu.wedgess.recipefinder.data.mappers.RecipeMapper
import eu.wedgess.recipefinder.helpers.TestHelper
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * This class tests that [RecipeData] is mapped to [RecipeEntity] and vice-versa
 *
 */
class RecipeMapperTest {

    private lateinit var mapper: RecipeMapper

    @Before
    fun setup() {
        mapper = RecipeMapper()
    }

    /**
     * Given - data of [RecipeData]
     * When - [RecipeMapper.mapToEntity] is called with data
     * Then - mapped entity is equal to expectedEntity
     */
    @Test
    fun dataMapsToEntity() {
        val data = TestHelper.getRecipeDataByName("Cheesecake")
        val expectedEntity = TestHelper.getRecipeEntityByName("Cheesecake")
        assertEquals(expectedEntity, mapper.mapToEntity(data))
    }

    /**
     * Given - entity of [RecipeEntity]
     * When - [RecipeMapper.mapFromEntity] is called with entity
     * Then - mapped data is equal to expectedData
     */
    @Test
    fun entityMapsToData() {
        val expectedData = TestHelper.getRecipeDataByName("Meatball")
        val entity = TestHelper.getRecipeEntityByName("Meatball")
        assertEquals(expectedData, mapper.mapFromEntity(entity))
    }

}