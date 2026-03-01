package com.bashar.avalag.src.features.basics.domain.usecases

import com.bashar.avalag.src.features.basics.domain.model.BasicsInfo
import com.bashar.avalag.src.features.basics.domain.model.Country
import com.bashar.avalag.src.features.basics.domain.model.Promotion
import com.bashar.avalag.src.features.basics.domain.repositories.IBasicsRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GetBasicsInfoUseCaseTest {

    private class FakeBasicsRepo : IBasicsRepo {
        var getBasicsCalls = 0

        var basicsToReturn: BasicsInfo = BasicsInfo(
            promotions = listOf(
                Promotion(
                    id = 1,
                    title = "test",
                    description = "desc",
                    image = "http://localhost/promo.png"
                )
            ),
            countries = listOf(
                Country(
                    id = 213,
                    name = "Syria",
                    currency = "l.s"
                )
            )
        )

        var throwOnGetBasics: Throwable? = null

        override suspend fun getBasicsInfo(): BasicsInfo {
            getBasicsCalls++
            throwOnGetBasics?.let { throw it }
            return basicsToReturn
        }

        override suspend fun getEnums() = error("Not needed in this test")
    }

    @Test
    fun `usecases returns basics info from repo`() = runBlocking {
        val repo = FakeBasicsRepo()
        val useCase = GetBasicsInfoUseCase(repo)

        val result = useCase()

        assertEquals(1, repo.getBasicsCalls)
        assertEquals(1, result.promotions.size)
        assertEquals("test", result.promotions.first().title)
        assertEquals(1, result.countries.size)
        assertEquals("Syria", result.countries.first().name)
    }

    @Test
    fun `usecase propagates repo exception`() = runBlocking {
        val repo = FakeBasicsRepo().apply {
            throwOnGetBasics = IllegalArgumentException("bad")
        }
        val useCase = GetBasicsInfoUseCase(repo)

        val ex = runCatching { useCase() }.exceptionOrNull()

        assertNotNull(ex)
        assertTrue(ex is IllegalArgumentException)
        assertEquals("bad", ex?.message)
        assertEquals(1, repo.getBasicsCalls)
    }
}