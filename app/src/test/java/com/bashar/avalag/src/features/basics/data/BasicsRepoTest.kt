package com.bashar.avalag.src.features.basics.data

import com.bashar.avalag.src.core.data.remote.model.ApiEnvelope
import com.bashar.avalag.src.features.basics.data.remote.BasicsApi
import com.bashar.avalag.src.features.basics.data.remote.dto.BasicsInfoDto
import com.bashar.avalag.src.features.basics.data.remote.dto.CountryDto
import com.bashar.avalag.src.features.basics.data.remote.dto.EnumsEnvelopeDto
import com.bashar.avalag.src.features.basics.data.remote.dto.PromotionDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BasicsRepoTest {

    private class FakeBasicsApi(
        private val enumsResponse: ApiEnvelope<EnumsEnvelopeDto>,
        private val basicsResponse: ApiEnvelope<BasicsInfoDto>
    ) : BasicsApi {

        var getEnumsCalls: Int = 0
        var getBasicsCalls: Int = 0

        override suspend fun getEnums(): ApiEnvelope<EnumsEnvelopeDto> {
            getEnumsCalls++
            return enumsResponse
        }

        override suspend fun getBasicsInfo(): ApiEnvelope<BasicsInfoDto> {
            getBasicsCalls++
            return basicsResponse
        }
    }

    @Test
    fun `repo maps enums response`() = runBlocking {
        val api = FakeBasicsApi(
            enumsResponse = ApiEnvelope(
                status = "success",
                message = "enums of app",
                data = EnumsEnvelopeDto(
                    enums = mapOf(
                        "payment_methods" to mapOf(
                            "cash" to "Cash",
                            "electronic" to "Electronic"
                        ),
                        "notifications_status" to mapOf(
                            "new" to "New",
                            "completed" to "Completed"
                        )
                    )
                )
            ),
            basicsResponse = ApiEnvelope(
                status = "success",
                message = "Info fetched successfully",
                data = BasicsInfoDto(
                    promotions = emptyList(),
                    countries = emptyList()
                )
            )
        )

        val repo = BasicsRepo(api)

        val enums = repo.getEnums()

        assertEquals(1, api.getEnumsCalls)
        assertTrue(enums.categories.containsKey("payment_methods"))
        assertEquals("Cash", enums.categories["payment_methods"]?.get("cash"))
        assertEquals("Completed", enums.categories["notifications_status"]?.get("completed"))
    }

    @Test
    fun `repo throws when enums data missing`() = runBlocking {
        val api = FakeBasicsApi(
            enumsResponse = ApiEnvelope(
                status = "success",
                message = "enums of app",
                data = null
            ),
            basicsResponse = ApiEnvelope(
                status = "success",
                message = "Info fetched successfully",
                data = BasicsInfoDto(promotions = emptyList(), countries = emptyList())
            )
        )

        val repo = BasicsRepo(api)

        val ex = runCatching { repo.getEnums() }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex!!.message?.contains("Missing data", ignoreCase = true) == true)
    }

    @Test
    fun `repo throws when enums missing in envelope`() = runBlocking {
        val api = FakeBasicsApi(
            enumsResponse = ApiEnvelope(
                status = "success",
                message = "enums of app",
                data = EnumsEnvelopeDto(enums = null)
            ),
            basicsResponse = ApiEnvelope(
                status = "success",
                message = "Info fetched successfully",
                data = BasicsInfoDto(promotions = emptyList(), countries = emptyList())
            )
        )

        val repo = BasicsRepo(api)

        val ex = runCatching { repo.getEnums() }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex!!.message?.contains("Missing enums", ignoreCase = true) == true)
    }

    @Test
    fun `repo maps basics response`() = runBlocking {
        val api = FakeBasicsApi(
            enumsResponse = ApiEnvelope(
                status = "success",
                message = "enums of app",
                data = EnumsEnvelopeDto(enums = emptyMap())
            ),
            basicsResponse = ApiEnvelope(
                status = "success",
                message = "Info fetched successfully",
                data = BasicsInfoDto(
                    promotions = listOf(
                        PromotionDto(
                            id = 1,
                            title = "test",
                            description = "description",
                            image = "http://localhost:8000/uploads/promotions/test.png"
                        )
                    ),
                    countries = listOf(
                        CountryDto(
                            id = 213,
                            name = "Syria",
                            currency = "l.s"
                        )
                    )
                )
            )
        )

        val repo = BasicsRepo(api)

        val info = repo.getBasicsInfo()

        assertEquals(1, api.getBasicsCalls)

        assertEquals(1, info.promotions.size)
        assertEquals(1, info.promotions[0].id)
        assertEquals("test", info.promotions[0].title)
        assertEquals("http://localhost:8000/uploads/promotions/test.png", info.promotions[0].image)

        assertEquals(1, info.countries.size)
        assertEquals(213, info.countries[0].id)
        assertEquals("Syria", info.countries[0].name)
        assertEquals("l.s", info.countries[0].currency)
    }

    @Test
    fun `repo throws when basics data missing`() = runBlocking {
        val api = FakeBasicsApi(
            enumsResponse = ApiEnvelope(
                status = "success",
                message = "enums of app",
                data = EnumsEnvelopeDto(enums = emptyMap())
            ),
            basicsResponse = ApiEnvelope(
                status = "success",
                message = "Info fetched successfully",
                data = null
            )
        )

        val repo = BasicsRepo(api)

        val ex = runCatching { repo.getBasicsInfo() }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex!!.message?.contains("Missing data", ignoreCase = true) == true)
    }
}